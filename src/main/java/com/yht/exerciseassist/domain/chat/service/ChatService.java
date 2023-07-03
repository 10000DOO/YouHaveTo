package com.yht.exerciseassist.domain.chat.service;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.chat.ChatList;
import com.yht.exerciseassist.domain.chat.ChatMessage;
import com.yht.exerciseassist.domain.chat.ChatRoom;
import com.yht.exerciseassist.domain.chat.MessageType;
import com.yht.exerciseassist.domain.chat.dto.ChatRoomListDto;
import com.yht.exerciseassist.domain.chat.dto.MessageResDto;
import com.yht.exerciseassist.domain.chat.dto.SendMessageReqDto;
import com.yht.exerciseassist.domain.chat.repository.ChatListRepository;
import com.yht.exerciseassist.domain.chat.repository.ChatMessageRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class ChatService {

    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatListRepository chatListRepository;
    private final ChatMessageRepository chatMessageRepository;

    //채팅방 불러오기
    public ResponseResult<List<ChatRoomListDto>> findAllRoom() {
        Member findMember = memberRepository.findByNotDeletedUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_MEMBER.getMessage()));

        ChatList findChatList = chatListRepository.findByMemberId(findMember.getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_CHATLIST.getMessage()));

        List<ChatRoomListDto> chatRoomList = new ArrayList<>();
        for (ChatRoom chatRoom : findChatList.getChatRooms()) {
            ChatRoomListDto chatRoomListDto = ChatRoomListDto.builder()
                    .roomId(chatRoom.getId())
                    .roomName(chatRoom.getRoomName())
                    .latestContent(chatRoom.getChatMessages().get(chatRoom.getChatMessages().size()-1).getChatContent())
                    .latestContentCreatedAt(chatRoom.getChatMessages().get(chatRoom.getChatMessages().size()-1).getDateTime().getUpdatedAt())
                    .build();

            chatRoomList.add(chatRoomListDto);
        }

        return new ResponseResult<>(HttpStatus.OK.value(), chatRoomList);
    }

    //채팅방 생성
    public ResponseResult<String> createRoom(String roomName) {
        Member findMember = memberRepository.findByNotDeletedUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_MEMBER.getMessage()));

        ChatList chatList;
        if(chatListRepository.findByMemberId(findMember.getId()).isEmpty()){
            chatList = new ChatList(findMember);
            chatListRepository.save(chatList);
        } else {
            chatList = chatListRepository.findByMemberId(findMember.getId()).get();
        }
        ChatRoom chatRoom = ChatRoom.builder()
                .roomName(roomName)
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                .chatList(chatList)
                .build();

        chatList.addChatRoom(chatRoom);
        chatRoomRepository.save(chatRoom);

        return new ResponseResult<>(HttpStatus.CREATED.value(), roomName);
    }

    public ResponseResult<MessageResDto> enterMessage(Long roomId) {
        ChatRoom findChatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_CHATLIST.getMessage()));
        Member findMember = memberRepository.findByNotDeletedUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_MEMBER.getMessage()));

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(findChatRoom)
                .chatContent(SecurityUtil.getCurrentUsername() + "님 채팅방에 입장하셨습니다.")
                .sender(findMember)
                .messageType(MessageType.ENTER)
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                .build();

        chatMessageRepository.save(chatMessage);
        MessageResDto messageResDto = new MessageResDto(findChatRoom.getId(), chatMessage.getChatContent());
        return new ResponseResult<>(HttpStatus.OK.value(), messageResDto);
    }

    public ResponseResult<MessageResDto> sendMessage(SendMessageReqDto sendMessageReqDto) {
        ChatRoom findChatRoom = chatRoomRepository.findById(sendMessageReqDto.getRoomId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_CHATLIST.getMessage()));
        Member findMember = memberRepository.findByNotDeletedUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_MEMBER.getMessage()));

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(findChatRoom)
                .chatContent(sendMessageReqDto.getMessage())
                .sender(findMember)
                .messageType(MessageType.CHAT)
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                .build();

        chatMessageRepository.save(chatMessage);
        MessageResDto messageResDto = new MessageResDto(findChatRoom.getId(), chatMessage.getChatContent());
        return new ResponseResult<>(HttpStatus.OK.value(), messageResDto);
    }
}
