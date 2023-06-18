package com.yht.exerciseassist.domain.chat.dto;

import lombok.Data;

@Data
public class EnterRoomDto {
    private Long roomId;

    public EnterRoomDto(Long roomId) {
        this.roomId = roomId;
    }
}
