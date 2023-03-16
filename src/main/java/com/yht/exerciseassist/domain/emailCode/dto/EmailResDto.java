package com.yht.exerciseassist.domain.emailCode.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailResDto {

    private String target;
    private String code;

    public EmailResDto(String target, String code) {
        this.target = target;
        this.code = code;
    }
}
