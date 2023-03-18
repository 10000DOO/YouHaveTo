package com.yht.exerciseassist.domain.emailCode;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_code_id")
    private Long id;

    private String email;

    private String code;

    private LocalDateTime createdAt;

    public EmailCode(String email, String code) {
        this.email = email;
        this.code = code;
        this.createdAt = LocalDateTime.now();

    }
}
