package com.yht.exerciseassist.domain.chat.dto;

import lombok.Data;

@Data
public class MessageResDto {

    private Long roomId;
    private String chatContent;

    public MessageResDto(Long roomId, String chatContent) {
        this.roomId = roomId;
        this.chatContent = chatContent;
    }
}
