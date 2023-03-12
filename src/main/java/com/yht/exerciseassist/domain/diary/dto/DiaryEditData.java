package com.yht.exerciseassist.domain.diary.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class DiaryEditData {

    private String review;
    private List<ExerciseInfoResDto> exerciseInfo;
    private List<String> mediaList;

    @Builder
    public DiaryEditData(String review, List<ExerciseInfoResDto> exerciseInfo, List<String> mediaList) {
        this.review = review;
        this.exerciseInfo = exerciseInfo;
        this.mediaList = mediaList;
    }
}
