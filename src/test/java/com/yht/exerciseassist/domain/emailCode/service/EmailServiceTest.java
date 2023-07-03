package com.yht.exerciseassist.domain.emailCode.service;

import com.yht.exerciseassist.domain.emailCode.repository.EmailCodeRepository;
import com.yht.exerciseassist.domain.factory.EmailCodeFactory;
import com.yht.exerciseassist.util.ResponseResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
class EmailServiceTest {

    @Autowired
    EmailService emailService;
    @MockBean
    private EmailCodeRepository emailCodeRepository;
    @MockBean
    private JavaMailSender emailSender;
    @Test
    void verifyEmailCode() {
        //given
        String code = "123456789ABC";
        Mockito.when(emailCodeRepository.findByCode(code)).thenReturn(Optional.of(EmailCodeFactory.createEmailCode()));
        //when
        ResponseResult<String> result = emailService.verifyEmailCode(code);
        //then
        Assertions.assertThat(result.getStatus()).isEqualTo(200);
        Assertions.assertThat(result.getData()).isEqualTo("test@test.com");
    }
}
