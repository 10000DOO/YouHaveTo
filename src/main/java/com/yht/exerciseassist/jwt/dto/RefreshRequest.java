package com.yht.exerciseassist.jwt.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RefreshRequest {

    private String refreshToken;
}
