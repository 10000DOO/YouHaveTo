package com.yht.exerciseassist.domain.post.dto;

import com.yht.exerciseassist.domain.post.PostType;
import com.yht.exerciseassist.domain.post.WorkOutCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

@Data
public class PostListDto {

    private String username;
    private String profileImage;
    @Enumerated(EnumType.STRING)
    private PostType postType;
    @Enumerated(EnumType.STRING)
    private WorkOutCategory workOutCategory;
    private String createdAt;
    private String title;
    private Long postId;
    private int mediaListCount;
    private int likeCount;
    private Long commentCount;

    @Builder
    public PostListDto(String username, String profileImage, PostType postType, WorkOutCategory workOutCategory,
                       String createdAt, String title, int mediaListCount, int likeCount, Long commentCount, Long postId) {
        this.username = username;
        this.profileImage = profileImage;
        this.postType = postType;
        this.workOutCategory = workOutCategory;
        this.createdAt = createdAt;
        this.title = title;
        this.mediaListCount = mediaListCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.postId = postId;
    }
}
