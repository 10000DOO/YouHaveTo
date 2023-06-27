package com.yht.exerciseassist.domain.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class ChatRoomListDto {

    private Long roomId;
    private String roomName;
    private String latestContent;
    private String latestContentCreatedAt;

    @Builder
    public ChatRoomListDto(Long roomId, String roomName, String latestContent, String latestContentCreatedAt) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.latestContent = latestContent;
        this.latestContentCreatedAt = latestContentCreatedAt;
    }
}
