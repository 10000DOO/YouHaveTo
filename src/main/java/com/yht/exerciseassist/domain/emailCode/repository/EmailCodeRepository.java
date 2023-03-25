package com.yht.exerciseassist.domain.emailCode.repository;

import com.yht.exerciseassist.domain.emailCode.EmailCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailCodeRepository extends JpaRepository<EmailCode, Long> {

    Optional<EmailCode> findByCode(String code);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void deleteByCreatedAtBefore(@Param("fiveMinuteAgo") LocalDateTime fiveMinuteAgo);
}
