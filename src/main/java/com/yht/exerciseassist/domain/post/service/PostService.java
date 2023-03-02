package com.yht.exerciseassist.domain.post.service;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.media.service.MediaService;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.domain.post.Post;
import com.yht.exerciseassist.domain.post.dto.PostDetailRes;
import com.yht.exerciseassist.domain.post.dto.WritePostDto;
import com.yht.exerciseassist.domain.post.repository.PostRepository;
import com.yht.exerciseassist.exceoption.error.ErrorCode;
import com.yht.exerciseassist.jwt.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MediaService mediaService;
    @Value("${base.url}")
    private String baseUrl;

    public ResponseResult<String> savePost(WritePostDto writePostDto, List<MultipartFile> files) throws IOException {
        Member findMember = memberRepository.findByUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.NOT_FOUND_EXCEPTION_MEMBER.getMessage()));

        Post post = Post.builder()
                .title(writePostDto.getTitle())
                .content(writePostDto.getContent())
                .postWriter(findMember)
                .views(0L)
                .likeCount(0)
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

        log.info("사용자명 : " + findMember.getUsername() + " 게시글 등록 완료");
        return new ResponseResult<>(HttpStatus.CREATED.value(), writePostDto.getTitle());
    }

    public ResponseResult<PostDetailRes> getPostDetail(Long postId) {
        Post postById = postRepository.findNotDeletedById(postId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_POST.getMessage()));

        List<String> mediaList = new ArrayList<>();
        for (Media media : postById.getMediaList()) {
            mediaList.add(baseUrl + "/media/" + media.getId());
        }

        String profileImage;
        try {
            profileImage = baseUrl + "/media/" + postById.getPostWriter().getMedia().getId();
        } catch (NullPointerException e) {
            profileImage = null;
        }

        PostDetailRes postDetailRes = PostDetailRes.builder()
                .username(postById.getPostWriter().getUsername())
                .profileImage(profileImage)
                .title(postById.getTitle())
                .content(postById.getContent())
                .mediaList(mediaList)
                .views(postById.getViews())
                .likeCount(postById.getLikeCount())
                .createdAt(postById.getDateTime().getCreatedAt())
                .postType(postById.getPostType())
                .workOutCategory(postById.getWorkOutCategory())
                .build();

        log.info("Username : {} postId : {} 게시글 상세 조회 성공", SecurityUtil.getCurrentUsername(), postById.getId());
        return new ResponseResult<>(HttpStatus.OK.value(), postDetailRes);
    }
}
