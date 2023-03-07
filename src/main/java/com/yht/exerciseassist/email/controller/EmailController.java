package com.yht.exerciseassist.email.controller;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.email.dto.EmailReqDto;
import com.yht.exerciseassist.email.dto.EmailResDto;
import com.yht.exerciseassist.email.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/email")
    public ResponseEntity<ResponseResult<EmailResDto>> checkEmailCode(@RequestBody @Valid EmailReqDto emailReqDto) throws Exception {

        return ResponseEntity.status(HttpStatus.OK).body(emailService.sendSimpleMessage(emailReqDto));
    }
}
