package com.yht.exerciseassist.gpt.routine.dto;

import com.yht.exerciseassist.gpt.routine.HealthPurpose;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutineReq {

    @NotNull(message = "운동 목적을 입력하세요.")
    private HealthPurpose healthPurpose;

    @NotNull(message = "키를 입력하세요.")
    private Integer height;

    @NotNull(message = "몸무게를 입력하세요.")
    private Integer weight;

    @NotNull(message = "일주일간 운동하는 횟수를 입력하세요.")
    private Integer divisions;
}
