package com.yht.exerciseassist.domain.post.service;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.factory.MediaFactory;
import com.yht.exerciseassist.domain.factory.MemberFactory;
import com.yht.exerciseassist.domain.factory.PostFactory;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.media.service.MediaService;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.domain.post.Post;
import com.yht.exerciseassist.domain.post.dto.PostEditList;
import com.yht.exerciseassist.domain.post.dto.WritePostDto;
import com.yht.exerciseassist.domain.post.repository.PostRepository;
import com.yht.exerciseassist.jwt.SecurityUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
class PostServiceTest {

    private static MockedStatic<SecurityUtil> securityUtilMockedStatic;
    PostService postService;
    @Value("${file.dir}")
    private String fileDir;
    @MockBean
    private PostRepository postRepository;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private MediaService mediaService;
    @Value("${test.address}")
    private String testAddress;

    @AfterEach
    public void afterAll() {
        securityUtilMockedStatic.close();
    }

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, memberRepository, mediaService);
        securityUtilMockedStatic = mockStatic(SecurityUtil.class);
    }

    @Test
    public void savePost() throws IOException {
        //given
        WritePostDto writePostDto = PostFactory.writePostDto();

        Member member = MemberFactory.createTestMember();

        given(SecurityUtil.getCurrentUsername()).willReturn("username");
        Mockito.when(memberRepository.findByUsername(SecurityUtil.getCurrentUsername())).thenReturn(Optional.ofNullable(member));

        ResponseResult responseResult = new ResponseResult(HttpStatus.CREATED.value(), "테스트 제목");

        String fileName = "tuxCoding.jpg";
        MockMultipartFile mediaFile = new MockMultipartFile("files", fileName, "image/jpeg", new FileInputStream(testAddress + fileName));
        List<MultipartFile> mediaFileList = new ArrayList<>();
        mediaFileList.add(mediaFile);
        //when
        ResponseResult responseResult1 = postService.savePost(writePostDto, mediaFileList);

        //then
        assertThat(responseResult1).isEqualTo(responseResult);
    }

    @Test
    public void getPostEditData() throws IllegalAccessException {
        //given
        given(SecurityUtil.getMemberRole()).willReturn("USER");

        Member member = MemberFactory.createTestMember();
        Post testPost = PostFactory.createTestPost(member);
        Mockito.when(postRepository.findByIdWithRole(1L, SecurityUtil.getMemberRole())).thenReturn(Optional.ofNullable(testPost));

        given(SecurityUtil.getCurrentUsername()).willReturn("member1");

        PostEditList postEditList = PostEditList.builder()
                .title(testPost.getTitle())
                .content(testPost.getContent())
                .postType(testPost.getPostType())
                .workOutCategory(testPost.getWorkOutCategory())
                .mediaList(new ArrayList<>())
                .build();

        ResponseResult<PostEditList> postEditListResponseResult = new ResponseResult<>(200, postEditList);
        //when
        ResponseResult<PostEditList> postEditData = postService.getPostEditData(1L);
        //then
        Assertions.assertThat(postEditData).isEqualTo(postEditListResponseResult);
    }

    @Test
    public void editPost() throws IOException {
        //given
        WritePostDto writePostDto = PostFactory.writePostDto();
        given(SecurityUtil.getMemberRole()).willReturn("USER");
        String fileName = "tuxCoding.jpg";
        MockMultipartFile mediaFile = new MockMultipartFile("files", fileName, "image/jpeg", new FileInputStream(testAddress + fileName));
        List<MultipartFile> mediaFileList = new ArrayList<>();
        mediaFileList.add(mediaFile);
        Long postId = 1L;
        Member testMember = MemberFactory.createTestMember();
        Post testPost = PostFactory.createTestPost(testMember);

        Mockito.when(postRepository.findByIdWithRole(postId, SecurityUtil.getMemberRole())).thenReturn(Optional.ofNullable(testPost));

        ResponseResult responseResult = new ResponseResult(200, writePostDto.getTitle());
        //when
        ResponseResult<String> stringResponseResult = postService.editPost(writePostDto, mediaFileList, postId);
        //then
        assertThat(stringResponseResult).isEqualTo(responseResult);
    }

    @Test
    public void deletePost() throws IOException {
        //given
        Long postId = 1L;

        given(SecurityUtil.getCurrentUsername()).willReturn("member1");

        Member member = MemberFactory.createTestMember();

        Post testPost = PostFactory.createTestPost(member);
        testPost.setPostIdUsedOnlyTest(postId);

        Media media = MediaFactory.createTeatMedia(fileDir + "tuxCoding.jpg");
        media.setMediaIdUsedOnlyTest(1L);
        List<Media> mediaId = new ArrayList<>();
        mediaId.add(media);

        testPost.linkToMedia(mediaId);

        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(testPost));
        ResponseResult<Long> mockResult = new ResponseResult<>(HttpStatus.OK.value(), postId);
        //when
        ResponseResult<Long> ResponseResult = postService.deletePost(postId);
        //then
        assertThat(ResponseResult).isEqualTo(mockResult);
    }
}
