package com.yht.exerciseassist.domain.diary.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class DiaryListDto {

    private String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    private List<Calender> calenders;

    private int monthlyPercentage;

    public DiaryListDto(List<Calender> calenders, int monthlyPercentage) {
        this.calenders = calenders;
        this.monthlyPercentage = monthlyPercentage;
    }
}
