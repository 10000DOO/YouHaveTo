package com.yht.exerciseassist.domain.chat.controller;

import com.yht.exerciseassist.domain.chat.ChatMessage;
import com.yht.exerciseassist.domain.chat.dto.ChatDto;
import com.yht.exerciseassist.domain.chat.dto.EnterRoomDto;
import com.yht.exerciseassist.domain.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations sendingOperations;
    private final ChatService chatService;

    @MessageMapping("/chat/enter")
    public void enter(EnterRoomDto enterRoomDTO) {
        ChatMessage message = chatService.enterMessage(enterRoomDTO);
        sendingOperations.convertAndSend("/topic/chat/room/" + message.getChatRoom().getId(), message);
    }

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(ChatDto chatDto) {
        ChatMessage message = chatService.sendMessage(chatDto);
        sendingOperations.convertAndSend("/topic/chat/room/" + message.getChatRoom().getId(), message);
    }
}
