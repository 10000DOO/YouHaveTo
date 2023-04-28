package com.yht.exerciseassist.domain.accuse.dto;

import com.yht.exerciseassist.domain.accuse.AccuseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AccuseReq {

    @NotNull(message = "신고 타입을 선택해주세요.")
    private AccuseType accuseType;

    @NotBlank(message = "신고 내용을 입력해주세요.")
    @Size(max = 300, message = "신고 내용은 300자까지 작성 가능합니다.")
    private String content;

    public AccuseReq(AccuseType accuseType, String content) {
        this.accuseType = accuseType;
        this.content = content;
    }
}
