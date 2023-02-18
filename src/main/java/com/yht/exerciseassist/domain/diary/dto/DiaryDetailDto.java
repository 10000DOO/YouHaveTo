package com.yht.exerciseassist.domain.diary.dto;

import com.yht.exerciseassist.domain.DateTime;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DiaryDetailDto {
    private String exerciseDate;
    private String review;
    private List<ExerciseInfoDto> exerciseInfo = new ArrayList<>();
    private DateTime dateTime;
    private List<String> mediaList;

    @Builder
    public DiaryDetailDto(String exerciseDate, String review, List<ExerciseInfoDto> exerciseInfo, DateTime dateTime, List<String> mediaList) {
        this.exerciseDate = exerciseDate;
        this.review = review;
        this.exerciseInfo = exerciseInfo;
        this.dateTime = dateTime;
        this.mediaList = mediaList;
    }
}
