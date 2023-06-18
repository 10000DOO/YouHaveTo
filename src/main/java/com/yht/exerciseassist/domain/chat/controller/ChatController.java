package com.yht.exerciseassist.domain.chat.controller;

import com.yht.exerciseassist.domain.chat.dto.EnterRoomDto;
import com.yht.exerciseassist.domain.chat.dto.MessageResDto;
import com.yht.exerciseassist.domain.chat.dto.SendMessageReqDto;
import com.yht.exerciseassist.domain.chat.service.ChatService;
import com.yht.exerciseassist.util.ResponseResult;
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
    public void enter(EnterRoomDto enterRoomDto) {
        ResponseResult<MessageResDto> result = chatService.enterMessage(enterRoomDto.getRoomId());
        sendingOperations.convertAndSend("/topic/chat/room/" + result.getData().getRoomId(), result.getData().getChatContent());
    }

    @MessageMapping("/chat/sendMessage")
    public void sendMessage(SendMessageReqDto sendMessageReqDto) {
        ResponseResult<MessageResDto> result = chatService.sendMessage(sendMessageReqDto);
        sendingOperations.convertAndSend("/topic/chat/room/" + result.getData().getRoomId(), result.getData().getChatContent());
    }
}
