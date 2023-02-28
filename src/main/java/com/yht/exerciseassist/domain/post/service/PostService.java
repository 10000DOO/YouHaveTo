package com.yht.exerciseassist.domain.post.service;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.media.service.MediaService;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.domain.post.Post;
import com.yht.exerciseassist.domain.post.dto.WritePostDto;
import com.yht.exerciseassist.domain.post.repository.PostRepository;
import com.yht.exerciseassist.exceoption.error.ErrorCode;
import com.yht.exerciseassist.jwt.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final MediaService mediaService;

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
        ResponseResult<String> responseResult = new ResponseResult(HttpStatus.CREATED.value(), writePostDto.getTitle());
        return responseResult;
    }
}
