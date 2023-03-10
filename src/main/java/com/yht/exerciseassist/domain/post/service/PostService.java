package com.yht.exerciseassist.domain.post.service;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.DateTime;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MediaService mediaService;
    private final LikeCountRepository likeCountRepository;
    @Value("${base.url}")
    private String baseUrl;

    public ResponseResult<String> savePost(WritePostDto writePostDto, List<MultipartFile> files) throws IOException {
        Member findMember = memberRepository.findByUsername(SecurityUtil.getCurrentUsername())
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
            List<Media> mediaList = mediaService.uploadImageToFileSystem(files);
            post.linkToMedia(mediaList);
        }
        postRepository.save(post);

        log.info("???????????? : " + findMember.getUsername() + " ????????? ?????? ??????");
        return new ResponseResult<>(HttpStatus.CREATED.value(), writePostDto.getTitle());
    }

    public ResponseResult<PostDetailRes> getPostDetail(Long postId) {
        String memberRole = SecurityUtil.getMemberRole();
        Post postById = postRepository.findByIdWithRole(postId, memberRole)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_POST.getMessage()));

        List<String> mediaList = getMediaList(postById);

        String profileImage;
        try {
            profileImage = baseUrl + "/media/" + postById.getPostWriter().getMedia().getId();
        } catch (NullPointerException e) {
            profileImage = null;
        }
        boolean isPressed = false;
        List<LikeCount> likeCount = postById.getLikeCount();
        for (LikeCount count : likeCount) {
            if (count.getMember() == postById.getPostWriter()) {
                isPressed = true;
            } else {
                isPressed = false;
            }
        }

        PostDetailRes postDetailRes = PostDetailRes.builder()
                .username(postById.getPostWriter().getUsername())
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
                .isMine(SecurityUtil.getCurrentUsername().equals(postById.getPostWriter().getUsername()))
                .build();

        postById.pulsViews();
        log.info("Username : {} postId : {} ????????? ?????? ?????? ??????", SecurityUtil.getCurrentUsername(), postById.getId());
        return new ResponseResult<>(HttpStatus.OK.value(), postDetailRes);
    }

    public ResponseResult<PostEditList> getPostEditData(Long postId) throws IllegalAccessException {
        String memberRole = SecurityUtil.getMemberRole();
        Post post = postRepository.findByIdWithRole(postId, memberRole)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_POST.getMessage()));

        if (post.getPostWriter().getUsername().equals(SecurityUtil.getCurrentUsername())) {

            List<String> mediaList = getMediaList(post);

            PostEditList postEditList = PostEditList.builder()
                    .title(post.getTitle())
                    .content(post.getContent())
                    .postType(post.getPostType())
                    .workOutCategory(post.getWorkOutCategory())
                    .mediaList(mediaList)
                    .build();

            log.info("???????????? : " + SecurityUtil.getCurrentUsername() + " ????????? ?????? ????????? ?????? ??????");
            return new ResponseResult<>(HttpStatus.OK.value(), postEditList);
        } else {
            throw new IllegalAccessException("?????? ???????????? ????????????.");
        }
    }

    public ResponseResult<String> editPost(WritePostDto writePostDto, List<MultipartFile> files, Long postId) throws IOException {
        String memberRole = SecurityUtil.getMemberRole();
        Post post = postRepository.findByIdWithRole(postId, memberRole)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_POST.getMessage()));

        post.editPost(writePostDto.getTitle(), writePostDto.getContent(), writePostDto.getPostType(), writePostDto.getWorkOutCategory());
        post.getDateTime().updatedAtUpdate();

        if (files != null && !(files.isEmpty())) {
            mediaService.deletePostImage(postId);
            List<Media> mediaList = mediaService.uploadImageToFileSystem(files);
            post.linkToMedia(mediaList);
        }

        log.info("???????????? : " + SecurityUtil.getCurrentUsername() + " ????????? ?????? ??????");
        return new ResponseResult<>(HttpStatus.OK.value(), post.getTitle());
    }

    public ResponseResult<Long> deletePost(Long postId) throws IOException {
        Post postById = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_POST.getMessage()));

        mediaService.deletePostImage(postById.getId());
        postById.getDateTime().canceledAtUpdate();

        log.info("username : {}, {}??? ????????? ?????? ??????", SecurityUtil.getCurrentUsername(), postById.getId());
        return new ResponseResult<>(HttpStatus.OK.value(), postById.getId());
    }

    private List<String> getMediaList(Post post) {
        List<String> mediaList = new ArrayList<>();
        for (Media media : post.getMediaList()) {
            mediaList.add(baseUrl + "/media/" + media.getId());
        }
        return mediaList;
    }

    public ResponseResult<Long> updateLike(Long postId, boolean clicked) {
        String memberRole = SecurityUtil.getMemberRole();
        String username = SecurityUtil.getCurrentUsername();
        Post post = postRepository.findByIdWithRole(postId, memberRole)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_POST.getMessage()));
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_MEMBER.getMessage()));

        boolean isPressed = false;
        List<LikeCount> like = post.getLikeCount();
        for (LikeCount count : like) {
            if (count.getMember() == post.getPostWriter()) {
                isPressed = true;
            } else {
                isPressed = false;
            }
        }

        if (clicked == false) {
            if (isPressed) {
                throw new IllegalArgumentException(ErrorCode.ALREADY_PRESSED.getMessage());
            } else {
                likeCountRepository.save(new LikeCount(post, member));
                log.info("username : {}, ????????? : {} ????????? ??????", username, post.getId());
                return new ResponseResult<>(HttpStatus.OK.value(), post.getId());
            }
        } else {
            LikeCount likeCount = likeCountRepository.findByPost(post)
                    .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_LIKE_PRESSED.getMessage()));

            likeCountRepository.deleteByPost(post);
            post.getLikeCount().remove(likeCount);
            member.getLikeCount().remove(likeCount);
            log.info("username : {}, ????????? : {} ????????? ??????", username, post.getId());
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
                    .username(post.getPostWriter().getUsername())
                    .postType(post.getPostType())
                    .workOutCategory(post.getWorkOutCategory())
                    .createdAt(calculateTime)
                    .title(post.getTitle())
                    .postId(post.getId())
                    .mediaListCount(post.getMediaList().size())
                    .likeCount(post.getLikeCount().size())
                    //commentCount()
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
