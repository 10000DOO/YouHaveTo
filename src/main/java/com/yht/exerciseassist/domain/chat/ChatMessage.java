package com.yht.exerciseassist.domain.chat;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room")
    private ChatRoom chatRoom;

    private String chatContent;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member sender;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Embedded
    private DateTime dateTime;

    public void linkToChatMessage(ChatRoom chatRoom) {
        if(this.chatRoom != null) {
            this.chatRoom.getChatMessages().remove(this);
        }
        this.chatRoom = chatRoom;
        chatRoom.getChatMessages().add(this);
    }

    @Builder
    public ChatMessage(ChatRoom chatRoom, String chatContent, Member sender, MessageType messageType, DateTime dateTime) {
        this.chatRoom = chatRoom;
        this.chatContent = chatContent;
        this.sender = sender;
        this.messageType = messageType;
        this.dateTime = dateTime;
    }
}
