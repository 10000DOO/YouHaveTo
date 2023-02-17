package com.yht.exerciseassist.domain.member;

import com.yht.exerciseassist.domain.DateTime;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    public void createMember() {
        //given
        //when
        Member member = Member.builder()
                .username("member1")
                .email("test@test.com")
                .loginId("testId2")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .role(MemberType.USER)
                .password("testPassword1!")
                .field("서울시")
                .build();

        //then
        assertThat(member.getPassword()).isEqualTo("testPassword1!");
        assertThat(member.getRole()).isEqualTo(MemberType.USER);
        assertThat(member.getEmail()).isEqualTo("test@test.com");
        assertThat(member.getUsername()).isEqualTo("member1");
        assertThat(member.getLoginId()).isEqualTo("testId2");
        assertThat(member.getDateTime()).isEqualTo(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null));
        assertThat(member.getField()).isEqualTo("서울시");
    }

    @Test
    public void refreshToken() {
        //given
        Member member = Member.builder()
                .username("member1")
                .email("test@test.com")
                .loginId("testId2")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .role(MemberType.USER)
                .password("testPassword1!")
                .field("서울시")
                .build();

        //when
        member.updateRefreshToken("testRefreshToken1!");
        //then
        assertThat(member.getRefreshToken()).isEqualTo("testRefreshToken1!");
    }
}
