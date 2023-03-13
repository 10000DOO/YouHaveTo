package com.yht.exerciseassist.domain.emailCode.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailReqDto {

    @Email
    private String email;

    public EmailReqDto(String email) {
        this.email = email;
    }
}
