package com.yht.exerciseassist.domain.diary;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExerciseInfo {

    private String exerciseName;//운동 이름

    private int reps;//횟수

    private int exSetCount;//세트

    private boolean cardio; //유산소 = ture 무산소 = false

    private int cardioTime; //유산소 운동 시간

    private boolean finished;

    @Builder
    public ExerciseInfo(String exerciseName, int reps, int exSetCount, boolean cardio, int cardioTime, boolean finished) {
        this.exerciseName = exerciseName;
        this.reps = reps;
        this.exSetCount = exSetCount;
        this.cardio = cardio;
        this.cardioTime = cardioTime;
        this.finished = finished;
    }
}
