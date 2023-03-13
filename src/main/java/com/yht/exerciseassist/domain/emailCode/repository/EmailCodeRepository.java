package com.yht.exerciseassist.domain.emailCode.repository;

import com.yht.exerciseassist.domain.emailCode.EmailCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailCodeRepository extends JpaRepository<EmailCode, Long> {

    Optional<EmailCode> findByCode(String code);
}
