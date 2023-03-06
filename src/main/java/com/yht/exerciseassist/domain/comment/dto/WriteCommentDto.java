package com.yht.exerciseassist.domain.comment.dto;

import com.yht.exerciseassist.domain.comment.Comment;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
public class WriteCommentDto {
    private String commentWriter;
    @NotEmpty(message = "내용을 입력해주세요.")
    private String commentContent;
    @NotEmpty(message = "게시글 번호를 입력해주세요.")
    private Long postId;
    private Comment parent;

    @Builder
    public WriteCommentDto(String commentWriter, String commentContent, Long postId, Comment parent) {
        this.commentWriter = commentWriter;
        this.commentContent = commentContent;
        this.postId = postId;
        this.parent = parent;
    }
}
