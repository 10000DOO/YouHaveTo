package com.yht.exerciseassist.domain.chat.repository;

import com.yht.exerciseassist.domain.chat.ChatList;
import com.yht.exerciseassist.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatListRepository extends JpaRepository<ChatList, Long> {

    Optional<ChatList> findByMember(Member member);
}
