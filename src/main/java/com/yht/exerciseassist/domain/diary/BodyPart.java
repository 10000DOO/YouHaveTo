package com.yht.exerciseassist.domain.diary;

import lombok.Getter;

@Getter
public enum BodyPart {
    BACK("등"),
    CHEST("가슴"),
    SHOULDER("어깨"),
    LEGS("하체"),
    ABS("복근"),
    BICEP("이두"),
    TRICEP("삼두");

    private final String message;

    BodyPart(String message) {
        this.message = message;
    }
}
