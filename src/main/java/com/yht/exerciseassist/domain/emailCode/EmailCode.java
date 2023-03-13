package com.yht.exerciseassist.domain.emailCode;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public EmailCode(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
