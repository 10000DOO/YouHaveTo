package com.yht.exerciseassist.domain.factory;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.MemberType;
import com.yht.exerciseassist.domain.member.dto.SignInRequestDto;
import com.yht.exerciseassist.domain.member.dto.SignUpRequestDto;
import com.yht.exerciseassist.jwt.dto.TokenInfo;

public class MemberFactory {

    public static Member createTestMember() {
        Member member = Member.builder()
                .username("member1")
                .email("test@test.com")
                .loginId("testId1")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .role(MemberType.USER)
                .password("testPassword1!")
                .field("서울시")
                .build();
        return member;
    }

    public static SignUpRequestDto createTestSignUpRequestDto() {
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setUsername("member1");
        signUpRequestDto.setPassword("testPassword1!");
        signUpRequestDto.setEmail("test@test.com");
        signUpRequestDto.setLoginId("testId1");
        signUpRequestDto.setField("서울시");

        return signUpRequestDto;
    }

    public static SignInRequestDto createTestSignInRequestDto() {
        SignInRequestDto signInRequestDto = new SignInRequestDto("testId1", "testPassword1!");

        return signInRequestDto;
    }

    public static TokenInfo createTestTokenInfo() {
        TokenInfo tokenInfo = new TokenInfo("Bearer", "access", "refresh");

        return tokenInfo;
    }
}
