package com.yht.exerciseassist.domain.chat.dto;

import lombok.Data;

@Data
public class SendMessageReqDto {

    private Long roomId;
    private String message;
}
