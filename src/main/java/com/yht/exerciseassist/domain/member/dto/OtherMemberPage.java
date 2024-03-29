package com.yht.exerciseassist.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class OtherMemberPage {

    private String username;
    private String field;
    private String createdAt;
    private String profileImage;
    private long postCount;

    @Builder
    public OtherMemberPage(String username, String field, String createdAt, String profileImage, long postCount) {
        this.username = username;
        this.field = field;
        this.createdAt = createdAt;
        this.profileImage = profileImage;
        this.postCount = postCount;
    }
}
