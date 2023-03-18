package com.yht.exerciseassist.domain.comment.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class CommentListwithSliceDto {
    List<CommentListDto> CommentListDto;
    Boolean isFirst;
    Boolean hasNext;

    @Builder
    public CommentListwithSliceDto(List<CommentListDto> commentListDto, Boolean isFirst, Boolean hasNext) {
        this.CommentListDto = commentListDto;
        this.isFirst = isFirst;
        this.hasNext = hasNext;
    }
}
