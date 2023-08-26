package com.yht.exerciseassist.domain.post.service;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.comment.Comment;
import com.yht.exerciseassist.domain.comment.dto.CommentListDto;
import com.yht.exerciseassist.domain.comment.repository.CommentRepository;
import com.yht.exerciseassist.domain.likeCount.LikeCount;
import com.yht.exerciseassist.domain.likeCount.repository.LikeCountRepository;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.media.service.MediaService;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.domain.post.Post;
import com.yht.exerciseassist.domain.post.dto.*;
import com.yht.exerciseassist.domain.post.repository.PostRepository;
import com.yht.exerciseassist.exception.error.ErrorCode;
import com.yht.exerciseassist.util.ResponseResult;
import com.yht.exerciseassist.util.SecurityUtil;
import com.yht.exerciseassist.util.TimeConvertUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MediaService mediaService;
    private final LikeCountRepository likeCountRepository;
    private final CommentRepository commentRepository;

    public ResponseResult<String> savePost(WritePostDto writePostDto, List<MultipartFile> files) throws IOException {
        Member findMember = memberRepository.findByNotDeletedUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_MEMBER.getMessage()));

        Post post = Post.builder()
                .title(writePostDto.getTitle())
                .content(writePostDto.getContent())
                .postWriter(findMember)
                .views(0L)
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                .postType(writePostDto.getPostType())
                .workOutCategory(writePostDto.getWorkOutCategory())
                .build();

        if (files != null && !(files.isEmpty())) {
            List<Media> mediaList = mediaService.uploadMediaToFiles(files);
            post.linkToMedia(mediaList);
        }
        postRepository.save(post);

        log.info("사용자명 : " + findMember.getUsername() + " 게시글 등록 완료");
        return new ResponseResult<>(HttpStatus.CREATED.value(), writePostDto.getTitle());
    }

    public ResponseResult<PostDetailRes> getPostDetail(Long postId) {
        String memberRole = SecurityUtil.getMemberRole();
        Post postById = postRepository.findByIdWithRole(postId, memberRole)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_POST.getMessage()));

        List<String> mediaList = getMediaList(postById);

        String profileImage;
        try {
            profileImage = postById.getPostWriter().getMedia().getFilePath();
        } catch (NullPointerException e) {
            profileImage = null;
        }
        boolean isPressed = false;
        List<LikeCount> likeCount = postById.getLikeCount();
        for (LikeCount count : likeCount) {
            isPressed = count.getMember() == postById.getPostWriter();
        }

        List<Comment> findComments = postById.getComments();
        List<CommentListDto> sendComments = new ArrayList<>();
        for (Comment findComment : findComments) {
            if (findComment.getParent() == null && sendComments.size() < 3) {
                sendComments.add(
                        CommentListDto.builder()
                                .commentId(findComment.getId())
                                .username(Optional.ofNullable(findComment.getCommentWriter().getUsername()).isPresent() ? findComment.getCommentWriter().getUsername() : "알 수 없음")
                                .commentContext(findComment.getCommentContent())
                                .createdAt(findComment.getDateTime().getCreatedAt())
                                .profileImage(profileImage)
                                .childCount(findComment.getChild().size())
                                .build()
                );
            }
        }

        PostDetailRes postDetailRes = PostDetailRes.builder()
                .username(Optional.ofNullable(postById.getPostWriter().getUsername()).isPresent() ? postById.getPostWriter().getUsername() : "알 수 없음")
                .profileImage(profileImage)
                .title(postById.getTitle())
                .content(postById.getContent())
                .mediaList(mediaList)
                .views(postById.getViews() + 1)
                .likeCount(likeCount.size())
                .likePressed(isPressed)
                .createdAt(postById.getDateTime().getCreatedAt())
                .postType(postById.getPostType())
                .workOutCategory(postById.getWorkOutCategory())
                .comments(sendComments)
                .commentCount(findComments.size())
                .isMine(Objects.equals(SecurityUtil.getCurrentUsername(), postById.getPostWriter().getUsername()))
                .build();

        postById.pulsViews();
        log.info("Username : {} postId : {} 게시글 상세 조회 성공", SecurityUtil.getCurrentUsername(), postById.getId());
        return new ResponseResult<>(HttpStatus.OK.value(), postDetailRes);
    }

    public ResponseResult<PostEditList> getPostEditData(Long postId) throws IllegalAccessException {
        String memberRole = SecurityUtil.getMemberRole();
        Post post = postRepository.findByIdWithRole(postId, memberRole)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_POST.getMessage()));

        if (Objects.equals(post.getPostWriter().getUsername(), SecurityUtil.getCurrentUsername())) {

            List<String> mediaList = getMediaList(post);

            PostEditList postEditList = PostEditList.builder()
                    .title(post.getTitle())
                    .content(post.getContent())
                    .postType(post.getPostType())
                    .workOutCategory(post.getWorkOutCategory())
                    .mediaList(mediaList)
                    .build();

            log.info("사용자명 : " + SecurityUtil.getCurrentUsername() + " 게시글 수정 데이터 조회 성공");
            return new ResponseResult<>(HttpStatus.OK.value(), postEditList);
        } else {
            throw new IllegalAccessException(ErrorCode.NOT_MINE_POST.getMessage());
        }
    }

    public ResponseResult<String> editPost(WritePostDto writePostDto, List<MultipartFile> files, Long postId) throws IOException {
        String memberRole = SecurityUtil.getMemberRole();
        Post post = postRepository.findByIdWithRole(postId, memberRole)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_POST.getMessage()));

        post.editPost(writePostDto.getTitle(), writePostDto.getContent(), writePostDto.getPostType(), writePostDto.getWorkOutCategory());
        post.getDateTime().updatedAtUpdate();

        if (files != null && !(files.isEmpty())) {
            mediaService.deletePostMedia(postId);
            List<Media> mediaList = mediaService.uploadMediaToFiles(files);
            post.linkToMedia(mediaList);
        } else {
            mediaService.deletePostMedia(postId);
        }

        log.info("사용자명 : " + SecurityUtil.getCurrentUsername() + " 게시글 수정 완료");
        return new ResponseResult<>(HttpStatus.OK.value(), post.getTitle());
    }

    public ResponseResult<Long> deletePost(Long postId) throws IllegalAccessException {
        Post postById = postRepository.findNotDeletedById(postId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_POST.getMessage()));

        if ((Objects.equals(postById.getPostWriter().getUsername(), SecurityUtil.getCurrentUsername()))||(SecurityUtil.getMemberRole().equals("ADMIN"))) {

            String localTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            postById.getDateTime().canceledAtUpdate();
            commentRepository.deleteCommentByPostId(localTime, postById.getId());
            mediaService.deletePostMedia(postById.getId());
            likeCountRepository.deleteAllByPost(postById);
        } else {
            throw new IllegalAccessException(ErrorCode.NO_MATCHED_POST.getMessage());
        }

        log.info("username : {}, {}번 게시글 삭제 완료", SecurityUtil.getCurrentUsername(), postById.getId());
        return new ResponseResult<>(HttpStatus.OK.value(), postById.getId());

    }

    private List<String> getMediaList(Post post) {
        List<String> mediaList = new ArrayList<>();
        for (Media media : post.getMediaList()) {
            mediaList.add(media.getFilePath());
        }
        return mediaList;
    }

    public ResponseResult<Long> updateLike(Long postId, boolean clicked) {
        String memberRole = SecurityUtil.getMemberRole();
        String username = SecurityUtil.getCurrentUsername();
        Post post = postRepository.findByIdWithRole(postId, memberRole)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_POST.getMessage()));
        Member member = memberRepository.findByNotDeletedUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_MEMBER.getMessage()));

        boolean isPressed = false;
        List<LikeCount> like = post.getLikeCount();
        for (LikeCount count : like) {
            isPressed = count.getMember() == post.getPostWriter();
        }

        if (!clicked) {
            if (isPressed) {
                throw new IllegalArgumentException(ErrorCode.ALREADY_PRESSED.getMessage());
            } else {
                likeCountRepository.save(new LikeCount(post, member));
                log.info("username : {}, 게시글 : {} 좋아요 누름", username, post.getId());
                return new ResponseResult<>(HttpStatus.OK.value(), post.getId());
            }
        } else {
            LikeCount likeCount = likeCountRepository.findNotDeletedByPostAndMember(post, member)
                    .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_LIKE_PRESSED.getMessage()));

            likeCountRepository.deleteById(likeCount.getId());
            post.getLikeCount().remove(likeCount);
            member.getLikeCount().remove(likeCount);
            log.info("username : {}, 게시글 : {} 좋아요 취소", username, post.getId());
            return new ResponseResult<>(HttpStatus.OK.value(), post.getId());
        }
    }

    public ResponseResult<PostListWithSliceDto> getPostList(List<String> postTypeList, List<String> workOutCategories, String username, Pageable pageable) throws ParseException {
        String memberRole = SecurityUtil.getMemberRole();

        Slice<Post> postList = postRepository.postAsSearchType(memberRole, postTypeList, workOutCategories, username, pageable);
        List<Post> posts = postList.getContent();
        boolean hasNext = postList.hasNext();
        boolean isFirst = postList.isFirst();

        List<PostListDto> postListDtos = new ArrayList<>();

        for (Post post : posts) {

            SimpleDateFormat formatterDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateString = post.getDateTime().getCreatedAt();
            Date date;
            try {
                date = formatterDateTime.parse(dateString);
            } catch (ParseException e) {
                throw new ParseException(ErrorCode.DATE_FORMAT_EXCEPTION.getMessage(), e.getErrorOffset());
            }

            String calculateTime = TimeConvertUtil.calculateTime(date);

            if (calculateTime == null) {
                String[] splitString = post.getDateTime().getCreatedAt().split(" ");
                calculateTime = splitString[0];
            }

            PostListDto postListDto = PostListDto.builder()
                    .username(Optional.ofNullable(post.getPostWriter().getUsername()).isPresent() ? post.getPostWriter().getUsername() : "알 수 없음")
                    .postType(post.getPostType().getMessage())
                    .workOutCategory(post.getWorkOutCategory().getMessage())
                    .createdAt(calculateTime)
                    .title(post.getTitle())
                    .postId(post.getId())
                    .mediaListCount(post.getMediaList().size())
                    .likeCount(post.getLikeCount().size())
                    .commentCount(post.getComments().size())
                    .views(post.getViews())
                    .build();

            postListDtos.add(postListDto);
        }

        PostListWithSliceDto postListWithSliceDto = PostListWithSliceDto.builder()
                .postListDto(postListDtos)
                .hasNext(hasNext)
                .isFirst(isFirst)
                .build();

        return new ResponseResult<>(HttpStatus.OK.value(), postListWithSliceDto);
    }
}
