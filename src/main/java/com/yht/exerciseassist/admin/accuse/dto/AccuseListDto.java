package com.yht.exerciseassist.admin.accuse.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class AccuseListDto {
    String accuseType;
    String content;
    Long postId;
    Long commentId;
    String createdAt;

    @Builder
    public AccuseListDto(String accuseType, String content, Long postId, Long commentId, String createdAt) {
        this.accuseType = accuseType;
        this.content = content;
        this.postId = postId;
        this.commentId = commentId;
        this.createdAt = createdAt;
    }
}
