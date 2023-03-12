package com.yht.exerciseassist.domain.diary.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExerciseInfoResDto {

    private String exerciseName;

    private int reps;

    private int exSetCount;//세트

    private boolean cardio; //유산소 = ture 무산소 = false

    private int cardioTime; //유산소 운동 시간

    private String bodyPart;

    private boolean finished; //완료 여부

    @Builder
    public ExerciseInfoResDto(String exerciseName, int reps, int exSetCount, boolean cardio, int cardioTime, String bodyPart, boolean finished) {
        this.exerciseName = exerciseName;
        this.reps = reps;
        this.exSetCount = exSetCount;
        this.cardio = cardio;
        this.cardioTime = cardioTime;
        this.bodyPart = bodyPart;
        this.finished = finished;
    }


}
