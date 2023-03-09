package com.yht.exerciseassist.domain.comment;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.comment.dto.WriteCommentDto;
import com.yht.exerciseassist.domain.factory.CommentFactory;
import com.yht.exerciseassist.domain.factory.MemberFactory;
import com.yht.exerciseassist.domain.factory.PostFactory;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.post.Post;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class CommentTest {

    @Test
    public void createComment() {
        //given
        Member member = MemberFactory.createTestMember();
        Post post = PostFactory.createTestPost(member);

        //when
        Comment comment = CommentFactory.createTestComment(member, post);

        //then
        assertThat(comment.getCommentContent()).isEqualTo("테스트 댓글");
        assertThat(comment.getCommentWriter()).isEqualTo(member);
        assertThat(comment.getPost()).isEqualTo(post);
        assertThat(comment.getDateTime()).isEqualTo(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null));
    }

    @Test
    public void writeCommentDto() {
        //given

        //when
        WriteCommentDto commentDto = CommentFactory.writeCommentDto();

        //then
        assertThat(commentDto.getCommentContent()).isEqualTo("테스트 댓글");
        assertThat(commentDto.getPostId()).isEqualTo(1L);
        assertThat(commentDto.getParentId()).isEqualTo(1L);
    }
}