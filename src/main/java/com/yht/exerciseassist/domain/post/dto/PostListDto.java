package com.yht.exerciseassist.domain.post.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class PostListDto {

    private String username;
    private String postType;
    private String workOutCategory;
    private String createdAt;
    private String title;
    private Long postId;
    private int mediaListCount;
    private int likeCount;
    private Long views;
    private Long commentCount;

    @Builder
    public PostListDto(String username, String postType, String workOutCategory, String createdAt,
                       String title, int mediaListCount, int likeCount, Long views, Long commentCount, Long postId) {
        this.username = username;
        this.postType = postType;
        this.workOutCategory = workOutCategory;
        this.createdAt = createdAt;
        this.title = title;
        this.mediaListCount = mediaListCount;
        this.likeCount = likeCount;
        this.views = views;
        this.commentCount = commentCount;
        this.postId = postId;
    }
}
