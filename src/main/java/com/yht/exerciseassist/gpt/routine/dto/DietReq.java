package com.yht.exerciseassist.gpt.routine.dto;

import com.yht.exerciseassist.gpt.routine.HealthPurpose;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DietReq {

    @NotEmpty(message = "먹은 음식을 입력하세요.")
    List<String> food = new ArrayList<>();

    @NotNull(message = "운동 목적을 입력하세요.")
    HealthPurpose healthPurpose;

    @NotNull(message = "몸무게를 입력하세요.")
    private Integer weight;
}
