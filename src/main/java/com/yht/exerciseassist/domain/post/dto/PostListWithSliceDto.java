package com.yht.exerciseassist.domain.post.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PostListWithSliceDto {

    private List<PostListDto> postListDto;
    private Boolean hasNext;
    private Boolean isFirst;

    @Builder
    public PostListWithSliceDto(List<PostListDto> postListDto, Boolean hasNext, Boolean isFirst) {
        this.postListDto = postListDto;
        this.hasNext = hasNext;
        this.isFirst = isFirst;
    }

}
