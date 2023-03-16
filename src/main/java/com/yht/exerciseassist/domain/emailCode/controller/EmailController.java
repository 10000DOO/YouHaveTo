package com.yht.exerciseassist.domain.emailCode.controller;

import com.yht.exerciseassist.domain.emailCode.dto.EmailReqDto;
import com.yht.exerciseassist.domain.emailCode.dto.EmailResDto;
import com.yht.exerciseassist.domain.emailCode.service.EmailService;
import com.yht.exerciseassist.util.ResponseResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/email")
    public ResponseEntity<ResponseResult<EmailResDto>> emailConfirm(@RequestBody @Valid EmailReqDto emailReqDto) throws Exception {

        return ResponseEntity.status(HttpStatus.OK).body(emailService.sendSimpleMessage(emailReqDto));
    }
}
