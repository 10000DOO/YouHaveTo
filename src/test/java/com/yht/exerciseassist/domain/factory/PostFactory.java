package com.yht.exerciseassist.domain.factory;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.post.Post;
import com.yht.exerciseassist.domain.post.PostType;
import com.yht.exerciseassist.domain.post.WorkOutCategory;
import com.yht.exerciseassist.domain.post.dto.WritePostDto;

public class PostFactory {

    public static Post createTestPost(Member member) {
        Post post = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .postWriter(member)
                .views(0L)
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .postType(PostType.KNOWLEDGE)
                .workOutCategory(WorkOutCategory.HEALTH)
                .build();
        return post;
    }

    public static Post createTestPostQA(Member member) {
        Post post = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .postWriter(member)
                .views(0L)
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .postType(PostType.Q_AND_A)
                .workOutCategory(WorkOutCategory.JOGGING)
                .build();
        return post;
    }

    public static Post createTestPostQB(Member member) {
        Post post = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .postWriter(member)
                .views(0L)
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .postType(PostType.FREE)
                .workOutCategory(WorkOutCategory.YOGA)
                .build();
        return post;
    }

    public static Post createTestPostQC(Member member) {
        Post post = Post.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .postWriter(member)
                .views(0L)
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .postType(PostType.COMPETITION)
                .workOutCategory(WorkOutCategory.PILATES)
                .build();
        return post;
    }

    public static WritePostDto writePostDto() {

        return WritePostDto.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .postType(PostType.KNOWLEDGE)
                .workOutCategory(WorkOutCategory.HEALTH)
                .build();
    }
}
