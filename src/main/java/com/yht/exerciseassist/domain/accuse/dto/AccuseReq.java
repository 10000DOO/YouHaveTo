package com.yht.exerciseassist.domain.accuse.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yht.exerciseassist.domain.accuse.AccuseType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccuseReq {

    @NotNull(message = "신고 타입을 선택해주세요.")
    @Enumerated(EnumType.STRING)
    private AccuseType accuseType;

    @NotEmpty(message = "신고 내용을 입력해주세요.")
    private String content;

    @JsonCreator
    public AccuseReq(@JsonProperty("accuseType") AccuseType accuseType, @JsonProperty("content") String content) {
        this.accuseType = accuseType;
        this.content = content;
    }
}
