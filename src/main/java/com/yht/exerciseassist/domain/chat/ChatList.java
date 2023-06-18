package com.yht.exerciseassist.domain.chat;

import com.yht.exerciseassist.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_list_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "chatList")
    private List<ChatRoom> chatRooms = new ArrayList<>();

    public void addChatRoom(ChatRoom chatRoom) {
        this.chatRooms.add(chatRoom);
        if (chatRoom.getChatList() != this) {
            chatRoom.linkToChatList(this);
        }
    }

    public ChatList(Member member) {
        this.member = member;
    }
}
