package com.yht.exerciseassist.domain.chat;

import com.yht.exerciseassist.domain.DateTime;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ChatRoom {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    private String ChatTitle;

    @Embedded
    private DateTime dateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_list")
    private ChatList chatList;

    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> chatMessages = new ArrayList<>();
}
