package com.yht.exerciseassist.gpt.routine;

import lombok.Getter;

@Getter
public enum HealthPurpose {

    MUSCLE_GAIN("muscle gain"),
    LOOSING_WEIGHT("loosing weight");

    private final String message;

    HealthPurpose(String message) {
        this.message = message;
    }
}
