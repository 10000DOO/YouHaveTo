package com.yht.exerciseassist.domain.post.dto;

import com.yht.exerciseassist.domain.post.PostType;
import com.yht.exerciseassist.domain.post.WorkOutCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PostEditList {

    private String title;

    private String content;

    private PostType postType;

    private WorkOutCategory workOutCategory;

    private List<String> mediaList;

    @Builder
    public PostEditList(String title, String content, PostType postType, WorkOutCategory workOutCategory, List<String> mediaList) {
        this.title = title;
        this.content = content;
        this.postType = postType;
        this.workOutCategory = workOutCategory;
        this.mediaList = mediaList;
    }
}
