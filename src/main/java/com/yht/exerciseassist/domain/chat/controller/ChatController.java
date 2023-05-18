package com.yht.exerciseassist.domain.chat.controller;

import com.yht.exerciseassist.domain.chat.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat/{roomID}/enter")
    public void enter(@Payload ChatDto chatDto, @DestinationVariable String roomID) {
        synchronized (simpMessagingTemplate){
            simpMessagingTemplate.convertAndSend("/topic/chat/room/" + roomID, chatDto.getMesasage());
        }
    }

    @MessageMapping("/chat/{roomID}/sendMessage")
    public void sendMessage(@Payload ChatDto chatDto, @DestinationVariable String roomID) {
        synchronized (simpMessagingTemplate){
            simpMessagingTemplate.convertAndSend("/topic/chat/room/"+ roomID, chatDto.getMesasage());
        }
    }
}
