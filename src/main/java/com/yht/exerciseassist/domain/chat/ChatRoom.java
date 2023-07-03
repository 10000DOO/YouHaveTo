package com.yht.exerciseassist.domain.chat;

import com.yht.exerciseassist.domain.DateTime;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    private String roomName;

    @Embedded
    private DateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_list")
    private ChatList chatList;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> chatMessages = new ArrayList<>();

    public void linkToChatList(ChatList chatList) {
        if(this.chatList != null) {
            this.chatList.getChatRooms().remove(this);
        }
        this.chatList = chatList;
        chatList.getChatRooms().add(this);
    }

    public void addChatMessage(ChatMessage chatMessage) {
        this.chatMessages.add(chatMessage);
        if (chatMessage.getChatRoom() != this) {
            chatMessage.linkToChatMessage(this);
        }
    }

    @Builder
    public ChatRoom(String roomName, DateTime dateTime, ChatList chatList) {
        this.roomName = roomName;
        this.dateTime = dateTime;
        this.chatList = chatList;
    }
}
