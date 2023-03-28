package com.yht.exerciseassist.domain.emailCode.service;

import com.yht.exerciseassist.domain.emailCode.EmailCode;
import com.yht.exerciseassist.domain.emailCode.dto.EmailReqDto;
import com.yht.exerciseassist.domain.emailCode.dto.EmailResDto;
import com.yht.exerciseassist.domain.emailCode.repository.EmailCodeRepository;
import com.yht.exerciseassist.exception.error.ErrorCode;
import com.yht.exerciseassist.exception.error.MailSendFailException;
import com.yht.exerciseassist.util.ResponseResult;
import com.yht.exerciseassist.util.TempPassword;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final EmailCodeRepository emailCodeRepository;
    private final JavaMailSender emailSender;

    public ResponseResult<EmailResDto> sendSimpleMessage(EmailReqDto emailReqDto) throws Exception {
        String target = emailReqDto.getEmail();
        String secretKey = TempPassword.generateRandomString(10);
        MimeMessage message = createMessage(target, secretKey);
        try {//예외처리
            emailSender.send(message);
            emailCodeRepository.save(new EmailCode(target, secretKey));
        } catch (MailException es) {
            es.printStackTrace();
            throw new MailSendFailException(ErrorCode.WRONG_SEND_EMAIL.getMessage());
        }
        log.info("이메일 전송 성공");
        return new ResponseResult<>(HttpStatus.OK.value(), new EmailResDto(target, secretKey));
    }

    private MimeMessage createMessage(String target, String secretKey) throws Exception {
        log.info("보내는 대상 : " + target);
        log.info("인증 번호 : " + secretKey);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, target);//보내는 대상
        message.setSubject("YouHaveTo 이메일 인증");//제목

        String msgg = "";
        msgg += "<div style='margin:20px;'>";
        msgg += "<h1> 안녕하세요 YouHaveTo입니다. </h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 복사해 입력해주세요<p>";
        msgg += "<br>";
        msgg += "<p>감사합니다.<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += secretKey + "</strong><div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("yhthealthassist@gmail.com", "YouHaveTo"));//보내는 사람

        return message;
    }
}
