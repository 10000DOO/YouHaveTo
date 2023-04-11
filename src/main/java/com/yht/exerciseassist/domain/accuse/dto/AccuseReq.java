package com.yht.exerciseassist.domain.accuse.dto;

import com.yht.exerciseassist.domain.accuse.AccuseType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccuseReq {

    @NotNull(message = "신고 타입을 선택해주세요.")
    private AccuseType accuseType;

    @NotEmpty(message = "신고 내용을 입력해주세요.")
    private String content;

    public AccuseReq(AccuseType accuseType, String content) {
        this.accuseType = accuseType;
        this.content = content;
    }
}
