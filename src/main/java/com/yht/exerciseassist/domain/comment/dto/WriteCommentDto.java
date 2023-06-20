package com.yht.exerciseassist.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WriteCommentDto {
    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 300, message = "댓글은 300자까지 작성 가능합니다.")
    private String commentContent;
    private Long postId;
    private Long parentId;

    @Builder
    public WriteCommentDto(String commentContent, Long postId, Long parentId) {
        this.commentContent = commentContent;
        this.postId = postId;
        this.parentId = parentId;
    }
}
