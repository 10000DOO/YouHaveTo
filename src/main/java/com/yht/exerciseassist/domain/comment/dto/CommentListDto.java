package com.yht.exerciseassist.domain.comment.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CommentListDto {
    String username;
    String commentContext;
    String createdAt;
    String profileImage;
    int childCount;

    @Builder
    public CommentListDto(String username, String commentContext, String createdAt, String profileImage, int childCount) {
        this.username = username;
        this.commentContext = commentContext;
        this.createdAt = createdAt;
        this.profileImage = profileImage;
        this.childCount = childCount;
    }
}
