package com.yht.exerciseassist.domain.chat.controller;

import com.yht.exerciseassist.domain.chat.dto.CreateRoomDTO;
import com.yht.exerciseassist.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final ChatService chatService;

    // 채팅방 생성
    @PostMapping("/chat/room")
    public ResponseEntity createRoom(@RequestParam("roomName") String roomName) {

        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.createRoom(roomName));
    }

    // 모든 채팅방 목록 반환
    @GetMapping("/room/all")
    public ResponseEntity room() {
        return ResponseEntity.ok(chatService.findAllRoom());
    }

    // 채팅방 입장 화면
    @GetMapping("/room/enter")
    public ResponseEntity roomDetail(@RequestParam Long roomId) {
        return ResponseEntity.ok(chatService.isValidRoom(roomId));
    }

    @GetMapping("/chat/room/{roomId}")
    @ResponseBody
    public ResponseEntity roomInfo(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatService.getChatRoomByRoomId(roomId));
    }
}
