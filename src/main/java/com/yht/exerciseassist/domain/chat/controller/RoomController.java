package com.yht.exerciseassist.domain.chat.controller;

import com.yht.exerciseassist.domain.chat.dto.ChatRoomListDto;
import com.yht.exerciseassist.domain.chat.service.ChatService;
import com.yht.exerciseassist.util.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final ChatService chatService;

    // 채팅방 생성
    @PostMapping("/chat/room")
    public ResponseEntity<ResponseResult<String>> createRoom(@RequestParam("roomName") String roomName) {

        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.createRoom(roomName));
    }

    // 모든 채팅방 목록 반환
    @GetMapping("/chat/room/all")
    public ResponseEntity<ResponseResult<List<ChatRoomListDto>>> room() {

        return ResponseEntity.status(HttpStatus.OK).body(chatService.findAllRoom());
    }
}
