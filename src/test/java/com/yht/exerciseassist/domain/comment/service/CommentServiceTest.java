package com.yht.exerciseassist.domain.comment.service;

import com.yht.exerciseassist.domain.comment.Comment;
import com.yht.exerciseassist.domain.comment.dto.CommentListDto;
import com.yht.exerciseassist.domain.comment.dto.CommentListwithSliceDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
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
        Mockito.when(memberRepository.findByNotDeletedUsername(SecurityUtil.getCurrentUsername())).thenReturn(Optional.ofNullable(member));

        Post post = PostFactory.createTestPost(member);
        Mockito.when(postRepository.findNotDeletedById(writeCommentDto.getPostId())).thenReturn(Optional.ofNullable(post));

        ResponseResult responseResult = new ResponseResult(HttpStatus.CREATED.value(), "테스트 댓글");

        //when
        ResponseResult responseResult1 = commentService.saveComment(writeCommentDto);

        //then
        assertThat(responseResult1).isEqualTo(responseResult);
    }

    @Test
    public void deleteComment() throws IllegalAccessException {
        //given
        given(SecurityUtil.getCurrentUsername()).willReturn("member1");

        Member member = MemberFactory.createTestMember();

        Post post = PostFactory.createTestPost(member);

        Comment comment = CommentFactory.createTestComment(member, post);
        comment.setCommentIdUsedOnlyTest(1L);

        Mockito.when(commentRepository.findByNotDeleteId(1L)).thenReturn(Optional.of(comment));

        ResponseResult<Long> responseResult = new ResponseResult(HttpStatus.OK.value(), 1L);

        //when
        ResponseResult<Long> responseResult1 = commentService.deleteComment(1L);

        //then
        assertThat(responseResult1).isEqualTo(responseResult);
    }

    @Test
    public void getComment() throws ParseException, IllegalAccessException {
        //given
        given(SecurityUtil.getMemberRole()).willReturn("USER");
        given(SecurityUtil.getCurrentUsername()).willReturn("member1");
        Long postId = 1L;
        Long parentId = null;
        String username = "member1";
        Pageable pageable = PageRequest.of(0, 1);

        Member testMember = MemberFactory.createTestMember();
        Post testPost = PostFactory.createTestPost(testMember);
        Comment testComment = CommentFactory.createTestComment(testMember, testPost);
        List<Comment> commentList = new ArrayList<>();
        commentList.add(testComment);
        Slice<Comment> slice = new SliceImpl<>(commentList);

        Mockito.when(commentRepository.findParentAndChildComment(SecurityUtil.getMemberRole(), postId, parentId, username, pageable)).thenReturn(slice);

        CommentListDto commentListDto = CommentFactory.getCommentListDto();
        List<CommentListDto> commentListDtos = new ArrayList<>();
        commentListDtos.add(commentListDto);
        //when
        ResponseResult<CommentListwithSliceDto> comment = commentService.getComment(postId, parentId, username, pageable);
        //then
        assertThat(comment.getStatus()).isEqualTo(200);
        assertThat(comment.getData().getCommentListDto()).isEqualTo(commentListDtos);
    }
}
