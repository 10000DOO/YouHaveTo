package com.yht.exerciseassist.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PWDto {

    @NotBlank
    private String password;
}
