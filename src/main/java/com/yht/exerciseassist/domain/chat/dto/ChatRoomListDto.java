package com.yht.exerciseassist.domain.chat.dto;

import lombok.Data;

@Data
public class ChatRoomListDto {

    private Long roomId;
    private String roomName;
    private String latestContent;
    private String latestContentCreatedAt;
}
