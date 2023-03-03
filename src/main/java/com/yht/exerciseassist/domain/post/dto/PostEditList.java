package com.yht.exerciseassist.domain.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yht.exerciseassist.domain.post.PostType;
import com.yht.exerciseassist.domain.post.WorkOutCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PostEditList {

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Enumerated(EnumType.STRING)
    private WorkOutCategory workOutCategory;

    private List<String> mediaList;

    @JsonCreator
    @Builder
    public PostEditList(@JsonProperty("title") String title, @JsonProperty("content") String content,
                        @JsonProperty("postType") PostType postType, @JsonProperty("workOutCategory") WorkOutCategory workOutCategory,
                        @JsonProperty("mediaList") List<String> mediaList) {
        this.title = title;
        this.content = content;
        this.postType = postType;
        this.workOutCategory = workOutCategory;
        this.mediaList = mediaList;
    }
}
