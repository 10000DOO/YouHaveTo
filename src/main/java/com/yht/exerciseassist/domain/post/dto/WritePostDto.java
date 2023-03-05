package com.yht.exerciseassist.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yht.exerciseassist.domain.post.PostType;
import com.yht.exerciseassist.domain.post.WorkOutCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class WritePostDto {

    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;

    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "게시글 카테고리를 선택해주세요.")
    @Enumerated(EnumType.STRING)
    private PostType postType;

    @NotNull(message = "운동 종류를 선택해주세요.")
    @Enumerated(EnumType.STRING)
    private WorkOutCategory workOutCategory;

    @JsonCreator
    @Builder
    public WritePostDto(@JsonProperty("title") String title, @JsonProperty("content") String content,
                        @JsonProperty("postType") PostType postType, @JsonProperty("workOutCategory") WorkOutCategory workOutCategory) {
        this.title = title;
        this.content = content;
        this.postType = postType;
        this.workOutCategory = workOutCategory;
    }
}
