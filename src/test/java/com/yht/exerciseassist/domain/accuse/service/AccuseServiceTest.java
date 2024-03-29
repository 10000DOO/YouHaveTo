package com.yht.exerciseassist.domain.accuse.service;

import com.yht.exerciseassist.domain.accuse.Accuse;
import com.yht.exerciseassist.domain.accuse.dto.AccuseReq;
import com.yht.exerciseassist.domain.accuse.repository.AccuseRepository;
import com.yht.exerciseassist.domain.comment.Comment;
import com.yht.exerciseassist.domain.comment.repository.CommentRepository;
import com.yht.exerciseassist.domain.factory.AccuseFactory;
import com.yht.exerciseassist.domain.factory.CommentFactory;
import com.yht.exerciseassist.domain.factory.MemberFactory;
import com.yht.exerciseassist.domain.factory.PostFactory;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.post.Post;
import com.yht.exerciseassist.domain.post.repository.PostRepository;
import com.yht.exerciseassist.util.ResponseResult;
import com.yht.exerciseassist.util.SecurityUtil;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
class AccuseServiceTest {

    private static MockedStatic<SecurityUtil> securityUtilMockedStatic;
    AccuseService accuseService;
    @Autowired
    EntityManager em;
    @MockBean
    private AccuseRepository accuseRepository;
    @MockBean
    private PostRepository postRepository;
    @MockBean
    private CommentRepository commentRepository;

    @AfterEach
    public void afterAll() {
        securityUtilMockedStatic.close();
    }

    @BeforeEach
    void setUp() {
        accuseService = new AccuseService(accuseRepository, postRepository, commentRepository);
        securityUtilMockedStatic = mockStatic(SecurityUtil.class);
    }

    @Test
    public void savePostAccuse() {
        //given
        given(SecurityUtil.getMemberRole()).willReturn("USER");
        Member testMember = MemberFactory.createTestMember();
        Post testPost = PostFactory.createTestPost(testMember);
        Mockito.when(postRepository.findByIdWithRole(1L, SecurityUtil.getMemberRole()))
                .thenReturn(Optional.ofNullable(testPost));
        AccuseReq accuseReq = AccuseFactory.createAccuseReq();
        Accuse accuse = AccuseFactory.createPostAccuse(testPost);

        ResponseResult<Long> result = new ResponseResult<>(201, null);
        //when
        ResponseResult<Long> accuseResult = accuseService.savePostAccuse(1L, accuseReq);
        //then
        Assertions.assertThat(accuseResult).isEqualTo(result);
    }

    @Test
    public void saveCommentAccuse() {
        //given
        given(SecurityUtil.getMemberRole()).willReturn("USER");
        Member testMember = MemberFactory.createTestMember();
        Post testPost = PostFactory.createTestPost(testMember);
        Comment testComment = CommentFactory.createTestComment(testMember, testPost);
        Mockito.when(commentRepository.findByIdWithRole(1L, SecurityUtil.getMemberRole()))
                .thenReturn(Optional.ofNullable(testComment));
        AccuseReq accuseReq = AccuseFactory.createAccuseReq();
        Accuse accuse = AccuseFactory.createCommentAccuse(testComment);

        ResponseResult<Long> result = new ResponseResult<>(201, null);
        //when
        ResponseResult<Long> accuseResult = accuseService.saveCommentAccuse(1L, accuseReq);
        //then
        Assertions.assertThat(accuseResult).isEqualTo(result);
    }
}
