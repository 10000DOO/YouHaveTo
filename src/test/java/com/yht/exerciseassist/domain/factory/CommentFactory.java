package com.yht.exerciseassist.domain.factory;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.comment.Comment;
import com.yht.exerciseassist.domain.comment.dto.WriteCommentDto;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.post.Post;

public class CommentFactory {
    public static Comment createTestComment(Member member, Post post) {

        return Comment.builder()
                .post(post)
                .commentWriter(member)
                .commentContent("테스트 댓글")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .build();
    }

    public static Comment createTestChildComment(Member member, Post post) {

        return Comment.builder()
                .post(post)
                .commentWriter(member)
                .commentContent("테스트 대댓글")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .build();
    }

    public static WriteCommentDto writeCommentDto() {
        return WriteCommentDto.builder()
                .commentContent("테스트 댓글")
                .postId(1L)
                .parentId(1L)
                .build();
    }

    public static WriteCommentDto writeCommentDtoNotParent() {
        return WriteCommentDto.builder()
                .commentContent("테스트 댓글")
                .postId(1L)
                .build();
    }
}
