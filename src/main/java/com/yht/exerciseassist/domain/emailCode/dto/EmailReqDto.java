package com.yht.exerciseassist.domain.emailCode.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailReqDto {

    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;
}
