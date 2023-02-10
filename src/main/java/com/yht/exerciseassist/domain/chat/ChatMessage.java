package com.yht.exerciseassist.domain.chat;

import com.yht.exerciseassist.domain.DateTime;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Embedded
    private DateTime dateTime;
}
