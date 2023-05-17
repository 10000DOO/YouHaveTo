package com.yht.exerciseassist.domain.accuse;

import lombok.Getter;

@Getter
public enum AccuseGetType {
    POST("게시글"),
    COMMENT("댓글"),
    DONE("완료");

    private final String message;

    AccuseGetType(String message) {
        this.message = message;
    }
}
