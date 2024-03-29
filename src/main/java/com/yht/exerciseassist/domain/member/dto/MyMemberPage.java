package com.yht.exerciseassist.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class MyMemberPage {

    private String username;
    private String email;
    private String field;
    private String createdAt;
    private String profileImage;
    private long postCount;
    private long commentCount;

    @Builder
    public MyMemberPage(String username, String email, String field, String createdAt, String profileImage, long postCount, long commentCount) {
        this.username = username;
        this.email = email;
        this.field = field;
        this.createdAt = createdAt;
        this.profileImage = profileImage;
        this.postCount = postCount;
        this.commentCount = commentCount;
    }
}
