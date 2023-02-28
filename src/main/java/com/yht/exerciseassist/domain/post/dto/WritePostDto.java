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

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PostType postType;

    @NotNull
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
