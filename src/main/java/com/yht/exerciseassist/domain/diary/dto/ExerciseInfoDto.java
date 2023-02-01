package com.yht.exerciseassist.domain.diary.dto;

import lombok.Data;

@Data
public class ExerciseInfoDto {

    private String exerciseName;

    private int reps;

    private int exSetCount;//세트

    private boolean cardio; //유산소 = ture 무산소 = false

    private int cardioTime; //유산소 운동 시간
}
