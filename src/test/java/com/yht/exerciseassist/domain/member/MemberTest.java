package com.yht.exerciseassist.domain.member;

import com.yht.exerciseassist.domain.DateTime;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class MemberTest {

    @Test
    public void createMember() {
        //given
        Member member = Member.builder()
                .username("member1")
                .email("test@test.com")
                .loginId("testId2")
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        null))
                .role(MemberType.USER)
                .password("testPassword1!")
                .field("서울시")
                .build();
        //when
        boolean created = member.getDateTime().getCreatedAt().equals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        boolean updated = member.getDateTime().getUpdatedAt().equals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        //then
        assertThat(member.getPassword()).isEqualTo("testPassword1!");
        assertThat(member.getRole()).isEqualTo(MemberType.USER);
        assertThat(member.getEmail()).isEqualTo("test@test.com");
        assertThat(member.getUsername()).isEqualTo("member1");
        assertThat(member.getLoginId()).isEqualTo("testId2");
        assertThat(created).isTrue();
        assertThat(updated).isTrue();
        assertThat(member.getDateTime().getCanceledAt()).isEqualTo(null);
        assertThat(member.getField()).isEqualTo("서울시");
    }

    @Test
    public void refreshToken() {
        //given
        Member member = Member.builder()
                .username("member1")
                .email("test@test.com")
                .loginId("testId2")
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        null))
                .role(MemberType.USER)
                .password("testPassword1!")
                .field("서울시")
                .build();

        //when
        member.setRefreshToken("testRefreshToken1!");
        //then
        assertThat(member.getRefreshToken()).isEqualTo("testRefreshToken1!");
    }
}