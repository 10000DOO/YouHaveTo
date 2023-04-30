package com.yht.exerciseassist.domain.comment.service;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.comment.Comment;
import com.yht.exerciseassist.domain.comment.dto.CommentListDto;
import com.yht.exerciseassist.domain.comment.dto.CommentListwithSliceDto;
import com.yht.exerciseassist.domain.comment.dto.WriteCommentDto;
import com.yht.exerciseassist.domain.comment.repository.CommentRepository;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.domain.post.Post;
import com.yht.exerciseassist.domain.post.repository.PostRepository;
import com.yht.exerciseassist.exception.error.ErrorCode;
import com.yht.exerciseassist.util.ResponseResult;
import com.yht.exerciseassist.util.SecurityUtil;
import com.yht.exerciseassist.util.TimeConvertUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    @Value("${base.url}")
    private String baseUrl;

    public ResponseResult<String> saveComment(WriteCommentDto writeCommentDto) {
        Member findMember = memberRepository.findByNotDeletedUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_MEMBER.getMessage()));

        Post findPost = postRepository.findNotDeletedById(writeCommentDto.getPostId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_POST.getMessage()));

        Comment comment = Comment.builder()
                .post(findPost)
                .commentWriter(findMember)
                .commentContent(writeCommentDto.getCommentContent())
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                .build();

        if (writeCommentDto.getParentId() != null) {
            Comment parentComment = commentRepository.findParentCommentByParentId(writeCommentDto.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT.getMessage()));
            if (!Objects.equals(writeCommentDto.getPostId(), parentComment.getPost().getId())) {
                throw new EntityNotFoundException(ErrorCode.NOT_EXIST_SAME_POST_IN_PARENT_AND_CHILD_COMMENT.getMessage());
            } else if (parentComment.getParent() != null) {
                throw new EntityNotFoundException(ErrorCode.ALREADY_HAVE_PARENTCOMMENT.getMessage());
            } else {
                comment.connectChildParent(parentComment);
            }
        }

        commentRepository.save(comment);
        log.info("사용자명 : {}, {}번 게시글에 대한 댓글 등록 완료", findMember.getUsername(), writeCommentDto.getPostId());
        return new ResponseResult<>(HttpStatus.CREATED.value(), writeCommentDto.getCommentContent());
    }

    public ResponseResult<Long> deleteComment(Long commentId) throws IllegalAccessException {
        Comment commentById = commentRepository.findByIdWithRole(commentId, SecurityUtil.getMemberRole())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT.getMessage()));

        if (Objects.equals(commentById.getCommentWriter().getUsername(), SecurityUtil.getCurrentUsername())) {
            commentById.getDateTime().canceledAtUpdate();

            String localTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            commentRepository.deleteCommentByParentId(localTime, commentById.getId());

        } else {
            throw new IllegalAccessException(ErrorCode.NO_MATCHED_COMMENTID.getMessage());
        }

        log.info("username : {}, {}번 댓글 삭제 완료", SecurityUtil.getCurrentUsername(), commentById.getId());
        return new ResponseResult<>(HttpStatus.OK.value(), commentById.getId());
    }

    public ResponseResult<CommentListwithSliceDto> getComment(Long postId, Long parentId, String username, Pageable pageable) throws ParseException, IllegalAccessException {
        String memberRole = SecurityUtil.getMemberRole();

        Slice<Comment> commentList = null;
        if (Objects.equals(username, SecurityUtil.getCurrentUsername()) || username == null) {
            commentList = commentRepository.findParentAndChildComment(memberRole, postId, parentId, username, pageable);
        } else if (username.isEmpty() && !Objects.equals(username, SecurityUtil.getCurrentUsername())) {
            throw new IllegalAccessException(ErrorCode.NOT_MINE_COMMENT.getMessage());
        }

        if (commentList != null) {
            List<Comment> comments = commentList.getContent();
            boolean hasNext = commentList.hasNext();
            boolean isFirst = commentList.isFirst();

            if (commentList.getContent().size() == 0) {
                throw new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT.getMessage());
            }

            List<CommentListDto> commentListDtos = new ArrayList<>();

            for (Comment comment : comments) {
                SimpleDateFormat formatterDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String dateString = comment.getDateTime().getCreatedAt();
                Date date;
                try {
                    date = formatterDateTime.parse(dateString);
                } catch (ParseException e) {
                    throw new ParseException(ErrorCode.DATE_FORMAT_EXCEPTION.getMessage(), e.getErrorOffset());
                }

                String calculateTime = TimeConvertUtil.calculateTime(date);

                if (calculateTime == null) {
                    String[] splitString = comment.getDateTime().getCreatedAt().split(" ");
                    calculateTime = splitString[0];
                }

                String profileImage;
                try {
                    profileImage = baseUrl + "/media/" + comment.getCommentWriter().getMedia().getId();
                } catch (NullPointerException e) {
                    profileImage = null;
                }

                int childCount;
                if (comment.getParent() == null) {
                    childCount = comment.getChild().size();
                } else {
                    childCount = -1;
                }

                CommentListDto commentListDto = CommentListDto.builder()
                        .username(Optional.ofNullable(comment.getCommentWriter().getUsername()).isPresent() ? comment.getCommentWriter().getUsername() : "알 수 없음")
                        .commentContext(comment.getCommentContent())
                        .createdAt(calculateTime)
                        .profileImage(profileImage)
                        .childCount(childCount)
                        .build();

                commentListDtos.add(commentListDto);
            }

            CommentListwithSliceDto commentListWithSliceDto = CommentListwithSliceDto.builder()
                    .commentListDto(commentListDtos)
                    .hasNext(hasNext)
                    .isFirst(isFirst)
                    .build();

            log.info("username : {}, 댓글 조회 완료", SecurityUtil.getCurrentUsername());
            return new ResponseResult<>(HttpStatus.OK.value(), commentListWithSliceDto);
        } else {
            throw new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT.getMessage());
        }
    }
}
