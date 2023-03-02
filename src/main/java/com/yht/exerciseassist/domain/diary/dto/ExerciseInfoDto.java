package com.yht.exerciseassist.domain.diary.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yht.exerciseassist.domain.diary.BodyPart;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExerciseInfoDto {

    private String exerciseName;

    private int reps;

    private int exSetCount;//세트

    private boolean cardio; //유산소 = ture 무산소 = false

    private int cardioTime; //유산소 운동 시간

    @Enumerated(EnumType.STRING)
    private BodyPart bodyPart;

    private boolean finished; //완료 여부

    @JsonCreator
    @Builder
    public ExerciseInfoDto(@JsonProperty("exerciseName") String exerciseName, @JsonProperty("reps") int reps,
                           @JsonProperty("exSetCount") int exSetCount, @JsonProperty("cardio") boolean cardio,
                           @JsonProperty("cardioTime") int cardioTime, @JsonProperty("bodyPart") BodyPart bodyPart,
                           @JsonProperty("finished") boolean finished) {
        this.exerciseName = exerciseName;
        this.reps = reps;
        this.exSetCount = exSetCount;
        this.cardio = cardio;
        this.cardioTime = cardioTime;
        this.bodyPart = bodyPart;
        this.finished = finished;
    }


}
