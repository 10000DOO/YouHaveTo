package com.yht.exerciseassist.domain.chat.repository;

import com.yht.exerciseassist.domain.chat.ChatList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatListRepository extends JpaRepository<ChatList, Long> {

    @Query("select c from ChatList c where c.member.id = :memberId")
    Optional<ChatList> findByMemberId(@Param("memberId") Long memberId);
}
