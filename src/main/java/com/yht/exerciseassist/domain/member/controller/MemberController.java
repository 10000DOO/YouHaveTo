package com.yht.exerciseassist.domain.member.controller;

import com.yht.exerciseassist.domain.member.dto.*;
import com.yht.exerciseassist.domain.member.service.MemberService;
import com.yht.exerciseassist.jwt.dto.TokenInfo;
import com.yht.exerciseassist.util.ResponseResult;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseResult<String>> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto, @RequestParam("code") String code) {

        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.join(signUpRequestDto, code));
    }

    @PostMapping("/signin")
    public ResponseEntity<ResponseResult<TokenInfo>> signIn(@RequestBody @Valid SignInRequestDto signInRequestDto) {

        return ResponseEntity.status(HttpStatus.OK).body(memberService.signIn(signInRequestDto.getLoginId(), signInRequestDto.getPassword()));
    }

    @PatchMapping("/member/delete")
    public ResponseEntity<ResponseResult<Long>> memberDelete() {

        return ResponseEntity.status(HttpStatus.OK).body(memberService.deleteMember());
    }

    @GetMapping("/member/info")
    public ResponseEntity<ResponseResult> memberPage(@RequestParam("username") String username) {

        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMemberPage(username));
    }

    @GetMapping("/find/id")
    public ResponseEntity<ResponseResult<String>> findId(@RequestParam("code") @NotBlank(message = "인증 코드를 입력해주세요.") String code) {

        return ResponseEntity.status(HttpStatus.OK).body(memberService.findId(code));
    }

    @PatchMapping("/find/pw")
    public ResponseEntity<ResponseResult<String>> findPw(@RequestBody @Valid FindPWDto findPWDto) {

        return ResponseEntity.status(HttpStatus.OK).body(memberService.findPw(findPWDto));
    }

    @PatchMapping("/member/edit")
    public ResponseEntity<ResponseResult<String>> editMember(@RequestPart @Valid EditMemberDto editMemberDto,
                                                             @RequestPart(required = false) List<MultipartFile> files) throws IOException {

        return ResponseEntity.status(HttpStatus.OK).body(memberService.editMemberData(editMemberDto, files));
    }

    @PostMapping("/member/password/check")
    public ResponseEntity<ResponseResult<Long>> passwordCheck(@RequestBody PasswordCheckDto passwordCheckDto) {

        return ResponseEntity.status(HttpStatus.OK).body(memberService.passwordValidation(passwordCheckDto));
    }
}
