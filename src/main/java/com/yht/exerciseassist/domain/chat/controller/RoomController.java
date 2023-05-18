//package com.yht.exerciseassist.domain.chat.controller;
//
//import com.yht.exerciseassist.domain.chat.dto.CreateRoomDTO;
//import com.yht.exerciseassist.domain.chat.service.ChatService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//
//@RestController
//@RequiredArgsConstructor
//public class RoomController {
//
//    private final ChatService chatService;
//
//    @PostMapping("/chat/room")
//    public ResponseEntity createRoom(@Validated @RequestBody CreateRoomDTO createRoomDTO) {
//        return ResponseEntity.ok(chatService.createRoom(createRoomDTO.getRoomName(), LocalDate.parse(createRoomDTO.getExpiredDate(), DateTimeFormatter.ISO_LOCAL_DATE), LocalTime.parse(createRoomDTO.getExpiredTime(), DateTimeFormatter.ISO_LOCAL_TIME)));
//    }
//
//    @GetMapping("/chat/room/{roomId}")
//    @ResponseBody
//    public ResponseEntity roomInfo(@PathVariable Integer roomId) {
//        return ResponseEntity.ok(chatService.getChatRoomByRoomId(roomId));
//    }
//}
