package com.yht.exerciseassist.domain.chat.repository;

import com.yht.exerciseassist.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
