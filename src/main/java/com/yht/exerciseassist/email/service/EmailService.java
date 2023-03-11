package com.yht.exerciseassist.email.service;

import com.yht.exerciseassist.email.dto.EmailReqDto;
import com.yht.exerciseassist.email.dto.EmailResDto;
import com.yht.exerciseassist.exception.error.MailSendFailException;
import com.yht.exerciseassist.util.ResponseResult;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    public static final String ePw = createKey();
    @Autowired
    JavaMailSender emailSender;

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 12; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) (rnd.nextInt(26) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) (rnd.nextInt(26) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }

    public ResponseResult<EmailResDto> sendSimpleMessage(EmailReqDto emailReqDto) throws Exception {
        String target = emailReqDto.getEmail();
        MimeMessage message = createMessage(target);
        try {//예외처리
            emailSender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
            throw new MailSendFailException("이메일 전송이 실패했습니다.");
        }
        return new ResponseResult<>(HttpStatus.OK.value(), new EmailResDto(target, ePw));
    }

    private MimeMessage createMessage(String target) throws Exception {
        log.info("보내는 대상 : " + target);
        log.info("인증 번호 : " + ePw);
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
        msgg += ePw + "</strong><div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("yhthealthassist@gmail.com", "YouHaveTo"));//보내는 사람

        return message;
    }
}
