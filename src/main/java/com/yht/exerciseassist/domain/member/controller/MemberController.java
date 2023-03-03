package com.yht.exerciseassist.domain.member.controller;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.member.dto.SignInRequestDto;
import com.yht.exerciseassist.domain.member.dto.SignUpRequestDto;
import com.yht.exerciseassist.domain.member.service.MemberService;
import com.yht.exerciseassist.jwt.dto.TokenInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseResult<String>> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.join(signUpRequestDto));
    }

    @PostMapping("/signin")
    public ResponseEntity<ResponseResult<TokenInfo>> signIn(@RequestBody @Valid SignInRequestDto signInRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.signIn(signInRequestDto.getLoginId(), signInRequestDto.getPassword()));
    }

    @PatchMapping("/member/delete")
    public ResponseEntity<ResponseResult<Long>> memberDelete() throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.deleteMember());
    }
}
