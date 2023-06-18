package com.yht.exerciseassist.domain.chat.service;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.chat.ChatList;
import com.yht.exerciseassist.domain.chat.ChatRoom;
import com.yht.exerciseassist.domain.chat.repository.ChatListRepository;
import com.yht.exerciseassist.domain.chat.repository.ChatRoomRepository;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.exception.error.ErrorCode;
import com.yht.exerciseassist.util.ResponseResult;
import com.yht.exerciseassist.util.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Service
@Slf4j
public class ChatService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatListRepository chatListRepository;

    //채팅방 불러오기
//    public ResponseResult<ChatRoomListDto> findAllRoom() {
//        Member findMember = memberRepository.findByNotDeletedUsername(SecurityUtil.getCurrentUsername())
//                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_MEMBER.getMessage()));
//
//        chatListRepository.
//    }

    //채팅방 하나 불러오기
//    public Room getChatRoomByRoomId(int roomId) {
//        return roomRepository.findRoomByRoomId(roomId).get();
//    }

    //채팅방 생성
    public ResponseResult<String> createRoom(String roomName) {
        Member findMember = memberRepository.findByNotDeletedUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_MEMBER.getMessage()));

        ChatList chatList = new ChatList(findMember);
        ChatRoom chatRoom = ChatRoom.builder()
                .roomName(roomName)
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                .chatList(chatList)
                .build();

        chatList.addChatRoom(chatRoom);

        chatListRepository.save(chatList);
        chatRoomRepository.save(chatRoom);
        return new ResponseResult<>(HttpStatus.CREATED.value(), roomName);
    }

//    public Message enterMessage(EnterRoomMessageDTO enterRoomMessageDTO) {
//        return messageRepository.save(enterRoomMessageDTO.toEntity(enterRoomMessageDTO.getRoomId(), enterRoomMessageDTO.getRoomName(), enterRoomMessageDTO.getNickname()));
//    }
//
//    public Message sendMessage(ChatMessageDTO chatMessageDTO){
//        return messageRepository.save(chatMessageDTO.toEntity(chatMessageDTO.getRoomId(), chatMessageDTO.getRoomName(), chatMessageDTO.getNickname(), chatMessageDTO.getContent()));
//    }
}
