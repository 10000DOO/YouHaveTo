package com.yht.exerciseassist.domain.post.dto;

import com.yht.exerciseassist.domain.post.PostType;
import com.yht.exerciseassist.domain.post.WorkOutCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class PostListDto {
    @NotEmpty
    private String username;

    private String profileImage;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Enumerated(EnumType.STRING)
    private WorkOutCategory workOutCategory;

    private String createdAt;
    @NotEmpty
    private String title;

    @NotNull
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
