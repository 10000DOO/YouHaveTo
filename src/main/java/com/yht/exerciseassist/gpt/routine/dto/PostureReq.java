package com.yht.exerciseassist.gpt.routine.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostureReq {

    @NotNull(message = "운동 이름을 입력하세요.")
    private String exerciseName;

    @NotNull(message = "근육 이름을 입력하세요.")
    private String muscleName;
}
