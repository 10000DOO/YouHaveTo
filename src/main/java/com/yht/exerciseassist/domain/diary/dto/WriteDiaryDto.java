package com.yht.exerciseassist.domain.diary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WriteDiaryDto {

    @NotNull(message = "운동 입력은 필수입니다.")
    private List<ExerciseInfoDto> exerciseInfo = new ArrayList<>();

    private String review;//후기 or 평가

    @NotBlank(message = "날짜 선택은 필수입니다.")
    private String exerciseDate; //운동 날짜
}
