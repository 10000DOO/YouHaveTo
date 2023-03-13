package com.yht.exerciseassist.domain.comment.service;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.comment.Comment;
import com.yht.exerciseassist.domain.comment.dto.WriteCommentDto;
import com.yht.exerciseassist.domain.comment.repository.CommentRepository;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.domain.post.Post;
import com.yht.exerciseassist.domain.post.repository.PostRepository;
import com.yht.exerciseassist.exception.error.ErrorCode;
import com.yht.exerciseassist.util.ResponseResult;
import com.yht.exerciseassist.util.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public ResponseResult<String> saveComment(WriteCommentDto writeCommentDto) {
        Member findMember = memberRepository.findByUsername(SecurityUtil.getCurrentUsername())
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
        Comment commentById = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_COMMENT.getMessage()));

        if (Objects.equals(commentById.getCommentWriter().getUsername(), SecurityUtil.getCurrentUsername())) {
            commentById.getDateTime().canceledAtUpdate();
        } else {
            throw new IllegalAccessException(ErrorCode.NO_MATCHED_COMMENTID.getMessage());
        }

        log.info("username : {}, {}번 댓글 삭제 완료", SecurityUtil.getCurrentUsername(), commentById.getId());
        return new ResponseResult<>(HttpStatus.OK.value(), commentById.getId());
    }
}
