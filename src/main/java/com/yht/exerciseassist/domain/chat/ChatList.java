package com.yht.exerciseassist.domain.chat;

import com.yht.exerciseassist.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ChatList {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "chat_list_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "chatList")
    private List<ChatRoom> chatRooms = new ArrayList<>();
}
