package com.yht.exerciseassist.domain.post.service;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.factory.MemberFactory;
import com.yht.exerciseassist.domain.factory.PostFactory;
import com.yht.exerciseassist.domain.media.service.MediaService;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.domain.post.dto.WritePostDto;
import com.yht.exerciseassist.domain.post.repository.PostRepository;
import com.yht.exerciseassist.jwt.SecurityUtil;
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
        MockMultipartFile mediaFile = new MockMultipartFile("files", fileName, "image/jpeg", new FileInputStream("/Users/10000doo/Documents/wallpaper/" + fileName));///Users/10000doo/Documents/wallpaper/Users/jeong-yunju/Documents/wallpaper
        List<MultipartFile> mediaFileList = new ArrayList<>();
        mediaFileList.add(mediaFile);
        //when
        ResponseResult responseResult1 = postService.savePost(writePostDto, mediaFileList);

        //then
        assertThat(responseResult1).isEqualTo(responseResult);
    }
}
