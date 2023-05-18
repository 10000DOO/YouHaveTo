package com.yht.exerciseassist.domain.chat.dto;

import lombok.Data;

@Data
public class ChatDto {
    private Integer channelId;
    private Integer writerId;
    private String mesasage;
}
