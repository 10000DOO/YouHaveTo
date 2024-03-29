package com.yht.exerciseassist.domain.factory;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.MemberType;
import com.yht.exerciseassist.domain.member.dto.MyMemberPage;
import com.yht.exerciseassist.domain.member.dto.OtherMemberPage;
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

    public static Member createTestAdmin() {
        Member admin = Member.builder()
                .username("member1")
                .email("test@test.com")
                .loginId("testId1")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .role(MemberType.ADMIN)
                .password("testPassword1!")
                .field("서울시")
                .build();
        return admin;
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
        TokenInfo tokenInfo = new TokenInfo("Bearer", "10000DOO", "access", "refresh");

        return tokenInfo;
    }

    public static MyMemberPage createMyMemberPage() {
        return MyMemberPage.builder()
                .username("member1")
                .email("test@test.com")
                .field("서울시")
                .createdAt("2023-02-11 11:11")
                .profileImage(null)
                .postCount(0)
                .build();
    }

    public static OtherMemberPage createOtherMemberPage() {
        return OtherMemberPage.builder()
                .username("member2")
                .field("서울시")
                .createdAt("2023-02-22")
                .profileImage("http://localhost:8080/media/1")
                .postCount(2)
                .build();
    }
}
