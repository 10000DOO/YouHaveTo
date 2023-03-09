package com.yht.exerciseassist.domain.comment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class WriteCommentDto {
    @NotEmpty(message = "내용을 입력해주세요.")
    private String commentContent;
    @NotNull(message = "게시글 번호를 입력해주세요.")
    private Long postId;
    private Long parentId;

    @Builder
    public WriteCommentDto(String commentContent, Long postId, Long parentId) {
        this.commentContent = commentContent;
        this.postId = postId;
        this.parentId = parentId;
    }
}
