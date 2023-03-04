package com.yht.exerciseassist.domain.post;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.factory.MemberFactory;
import com.yht.exerciseassist.domain.factory.PostFactory;
import com.yht.exerciseassist.domain.member.Member;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostTest {

    @Test
    public void createPost() {
        //given
        Member member = MemberFactory.createTestMember();
        //when
        Post post = PostFactory.createTestPost(member);
        //then
        assertThat(post.getTitle()).isEqualTo("테스트 제목");
        assertThat(post.getContent()).isEqualTo("테스트 내용");
        assertThat(post.getPostWriter()).isEqualTo(member);
        assertThat(post.getViews()).isEqualTo(0L);
        assertThat(post.getLikeCount().size()).isEqualTo(0);
        assertThat(post.getDateTime()).isEqualTo(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null));
        assertThat(post.getPostType()).isEqualTo(PostType.KNOWLEDGE);
        assertThat(post.getWorkOutCategory()).isEqualTo(WorkOutCategory.HEALTH);
    }
}
