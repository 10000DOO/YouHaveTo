package com.yht.exerciseassist.admin.accuse.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class AccuseListWithSliceDto {
    private List<AccuseListDto> accuseListDto;
    private Boolean hasNext;
    private Boolean isFirst;

    @Builder
    public AccuseListWithSliceDto(List<AccuseListDto> accuseListDto, Boolean hasNext, Boolean isFirst) {
        this.accuseListDto = accuseListDto;
        this.hasNext = hasNext;
        this.isFirst = isFirst;
    }
}
