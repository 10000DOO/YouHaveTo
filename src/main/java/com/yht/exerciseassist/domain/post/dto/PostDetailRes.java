package com.yht.exerciseassist.domain.post.dto;

import com.yht.exerciseassist.domain.comment.Comment;
import com.yht.exerciseassist.domain.post.PostType;
import com.yht.exerciseassist.domain.post.WorkOutCategory;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PostDetailRes {

    private String username;
    private String profileImage;
    private String title;
    private String content;
    private List<String> mediaList;
    private Long views;
    private int likeCount;
    private boolean likePressed;
    private String createdAt;
    private PostType postType;
    private WorkOutCategory workOutCategory;
    private List<Comment> comments;
    private boolean isMine;

    @Builder
    public PostDetailRes(String username, String profileImage, String title, String content, List<String> mediaList, Long views, int likeCount, boolean likePressed, String createdAt, PostType postType, WorkOutCategory workOutCategory, List<Comment> comments, boolean isMine) {
        this.username = username;
        this.profileImage = profileImage;
        this.title = title;
        this.content = content;
        this.mediaList = mediaList;
        this.views = views;
        this.likeCount = likeCount;
        this.likePressed = likePressed;
        this.createdAt = createdAt;
        this.postType = postType;
        this.workOutCategory = workOutCategory;
        this.comments = comments;
        this.isMine = isMine;
    }
}
