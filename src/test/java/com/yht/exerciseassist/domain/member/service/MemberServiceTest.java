package com.yht.exerciseassist.domain.member.service;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.MemberType;
import com.yht.exerciseassist.domain.member.dto.SignInRequestDto;
import com.yht.exerciseassist.domain.member.dto.SignUpRequestDto;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.jwt.JwtTokenProvider;
import com.yht.exerciseassist.jwt.dto.TokenInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
class MemberServiceTest {

    MemberService memberService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setup() {
        memberService = new MemberService(memberRepository, passwordEncoder, authenticationManagerBuilder, jwtTokenProvider);
    }

    @Test
    public void signUp() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setUsername("username");
        signUpRequestDto.setPassword("testPassword3!");
        signUpRequestDto.setEmail("test@test.com");
        signUpRequestDto.setLoginId("testId3");
        signUpRequestDto.setField("서울시");

        Member member = Member.builder()
                .username("username")
                .email("test@test.com")
                .loginId("testId3")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .role(MemberType.USER)
                .password("testPassword3!")
                .field("서울시")
                .build();

        Mockito.when(memberRepository.save(member)).thenReturn(member);
        //when
        ResponseResult response = memberService.join(signUpRequestDto);
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getData()).isEqualTo(member.getUsername());
    }

    @Test
    public void duplicatedSignUp() {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setUsername("username");
        signUpRequestDto.setPassword("testPassword3!");
        signUpRequestDto.setEmail("test@test.com");
        signUpRequestDto.setLoginId("testId3");
        signUpRequestDto.setField("서울시");

        Member member = Member.builder()
                .username("username")
                .email("test@test.com")
                .loginId("testId3")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .role(MemberType.USER)
                .password("testPassword3!")
                .field("서울시")
                .build();

        Mockito.when(memberRepository.findByLoginId(signUpRequestDto.getLoginId())).thenReturn(Optional.ofNullable(member));
        Mockito.when(memberRepository.findByEmail(signUpRequestDto.getEmail())).thenReturn(Optional.ofNullable(member));
        Mockito.when(memberRepository.findByUsername(signUpRequestDto.getUsername())).thenReturn(Optional.ofNullable(member));

        //when
        //then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            memberService.join(signUpRequestDto);
        });
    }

    @Test
    public void signIn() {
        //given
        SignInRequestDto signInRequestDto = new SignInRequestDto();
        signInRequestDto.setPassword("testPassword1!");
        signInRequestDto.setLoginId("testId3");

        Member member = Member.builder()
                .username("username")
                .email("test@test.com")
                .loginId(signInRequestDto.getLoginId())
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .role(MemberType.USER)
                .password(passwordEncoder.encode(signInRequestDto.getPassword()))
                .field("서울시")
                .build();

        Mockito.when(memberRepository.findByLoginId(signInRequestDto.getLoginId())).thenReturn(Optional.ofNullable(member));

        TokenInfo tokenInfo = new TokenInfo("Bearer", "access", "refresh");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(signInRequestDto.getLoginId(), signInRequestDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        Mockito.when(jwtTokenProvider.generateToken(authentication)).thenReturn(tokenInfo);

        //when
        ResponseResult response = memberService.signIn(signInRequestDto.getLoginId(), signInRequestDto.getPassword());
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThrows(InternalAuthenticationServiceException.class, () -> {
            memberService.signIn(signInRequestDto.getLoginId() + "1", signInRequestDto.getPassword());
        });
        Assertions.assertThrows(BadCredentialsException.class, () -> {
            memberService.signIn(signInRequestDto.getLoginId(), signInRequestDto.getPassword() + "2");
        });
    }
}
