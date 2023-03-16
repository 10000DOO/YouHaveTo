package com.yht.exerciseassist.domain.diary.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class DiaryDetailDto {
    private String exerciseDate;
    private String review;
    private List<ExerciseInfoResDto> exerciseInfo;
    private String createdAt;
    private List<String> mediaList;

    @Builder
    public DiaryDetailDto(String exerciseDate, String review, List<ExerciseInfoResDto> exerciseInfo, String createdAt, List<String> mediaList) {
        this.exerciseDate = exerciseDate;
        this.review = review;
        this.exerciseInfo = exerciseInfo;
        this.createdAt = createdAt;
        this.mediaList = mediaList;
    }
}
