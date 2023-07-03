package com.yht.exerciseassist.domain.chat.repository;

import com.yht.exerciseassist.domain.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
