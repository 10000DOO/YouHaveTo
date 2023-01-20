package com.yht.exerciseassist.domain.member.repository;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.MemberType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager em;

    @Test
    public void saveMember() {
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
        Member saveMember = memberRepository.save(member);
        //then
        assertThat(saveMember.getId()).isEqualTo(member.getId());
    }

    @Test
    public void findMember() {
        //given
        Member member = Member.builder()
                .username("member1")
                .email("test123@test.com")
                .loginId("testId2")
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        null))
                .role(MemberType.USER)
                .password("testPassword1!")
                .field("서울시")
                .build();

        member.setRefreshToken("refreshToken");

        Member saveMember = memberRepository.save(member);
        em.flush();
        em.clear();
        //when
        Optional<Member> byLoginId = memberRepository.findByLoginId(member.getLoginId());
        Optional<Member> byEmail = memberRepository.findByEmail(member.getEmail());
        Optional<Member> byUsername = memberRepository.findByUsername(member.getUsername());
        Optional<Member> byRefreshToken = memberRepository.findByRefreshToken(member.getRefreshToken());
        //then
        assertThat(byLoginId.get().getId()).isEqualTo(saveMember.getId());
        assertThat(byEmail.get().getId()).isEqualTo(saveMember.getId());
        assertThat(byUsername.get().getId()).isEqualTo(saveMember.getId());
        assertThat(byRefreshToken.get().getId()).isEqualTo(saveMember.getId());
    }
}