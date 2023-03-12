package com.yht.exerciseassist.domain.post;

import lombok.Getter;

@Getter
public enum PostType {
    Q_AND_A("Q&A"),
    KNOWLEDGE("지식 공유"),
    SHOW_OFF("자랑"),
    COMPETITION("평가"),
    FREE("자유");

    private final String message;

    PostType(String message) {
        this.message = message;
    }
}
