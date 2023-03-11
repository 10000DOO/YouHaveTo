package com.yht.exerciseassist.domain.comment.service;

import com.yht.exerciseassist.domain.comment.dto.WriteCommentDto;
import com.yht.exerciseassist.domain.comment.repository.CommentRepository;
import com.yht.exerciseassist.domain.factory.CommentFactory;
import com.yht.exerciseassist.domain.factory.MemberFactory;
import com.yht.exerciseassist.domain.factory.PostFactory;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.domain.post.Post;
import com.yht.exerciseassist.domain.post.repository.PostRepository;
import com.yht.exerciseassist.util.ResponseResult;
import com.yht.exerciseassist.util.SecurityUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
class CommentServiceTest {
    private static MockedStatic<SecurityUtil> securityUtilMockedStatic;
    @MockBean
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private PostRepository postRepository;
    @MockBean
    private MemberRepository memberRepository;

    @AfterEach
    public void afterAll() {
        securityUtilMockedStatic.close();
    }

    @BeforeEach
    void setUp() {
        commentService = new CommentService(commentRepository, postRepository, memberRepository);
        securityUtilMockedStatic = mockStatic(SecurityUtil.class);
    }

    @Test
    public void saveComment() {
        //given
        WriteCommentDto writeCommentDto = CommentFactory.writeCommentDtoNotParent();

        Member member = MemberFactory.createTestMember();

        given(SecurityUtil.getCurrentUsername()).willReturn("username");
        Mockito.when(memberRepository.findByUsername(SecurityUtil.getCurrentUsername())).thenReturn(Optional.ofNullable(member));

        Post post = PostFactory.createTestPost(member);
        Mockito.when(postRepository.findNotDeletedById(writeCommentDto.getPostId())).thenReturn(Optional.ofNullable(post));

        ResponseResult responseResult = new ResponseResult(HttpStatus.CREATED.value(), "테스트 댓글");

        //when
        ResponseResult responseResult1 = commentService.saveComment(writeCommentDto);

        //then
        assertThat(responseResult1).isEqualTo(responseResult);
    }


}
