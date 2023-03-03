package com.yht.exerciseassist.domain.diary.dto;

import com.yht.exerciseassist.domain.DateTime;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class DiaryEditData {

    private String review;
    private List<ExerciseInfoDto> exerciseInfo;
    private List<String> mediaList;

    @Builder
    public DiaryEditData(String review, List<ExerciseInfoDto> exerciseInfo, DateTime dateTime, List<String> mediaList) {
        this.review = review;
        this.exerciseInfo = exerciseInfo;
        this.mediaList = mediaList;
    }
}
