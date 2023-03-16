package com.yht.exerciseassist.domain.post;

import lombok.Getter;

@Getter
public enum WorkOutCategory {
    HEALTH("헬스"),
    PILATES("필라테스"),
    YOGA("요가"),
    JOGGING("조깅"),
    ETC("기타");

    private final String message;

    WorkOutCategory(String message) {
        this.message = message;
    }
}
