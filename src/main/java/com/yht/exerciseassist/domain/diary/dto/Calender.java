package com.yht.exerciseassist.domain.diary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Calender {

    private String exerciseDate;

    private int dailyPercentage;

    private Long diaryId;
}
