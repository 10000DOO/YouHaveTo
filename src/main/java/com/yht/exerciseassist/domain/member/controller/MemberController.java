package com.yht.exerciseassist.domain.member.controller;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.member.dto.SignInRequestDto;
import com.yht.exerciseassist.domain.member.dto.SignUpRequestDto;
import com.yht.exerciseassist.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        return memberService.join(signUpRequestDto);
    }

    @PostMapping("/signin")
    public ResponseEntity signIn(@RequestBody @Valid SignInRequestDto signInRequestDto) {
        return memberService.signIn(signInRequestDto.getLoginId(), signInRequestDto.getPassword());
    }
}
