package com.yht.exerciseassist.domain.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EnterRoomDto {
    @NotNull
    private Long roomId;
    @NotBlank
    private String roomName;
    @NotBlank
    private String username;
}
