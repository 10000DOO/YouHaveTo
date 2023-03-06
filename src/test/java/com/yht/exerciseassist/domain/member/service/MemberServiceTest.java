package com.yht.exerciseassist.domain.member.service;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.factory.MediaFactory;
import com.yht.exerciseassist.domain.factory.MemberFactory;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.media.service.MediaService;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.MemberType;
import com.yht.exerciseassist.domain.member.dto.SignInRequestDto;
import com.yht.exerciseassist.domain.member.dto.SignUpRequestDto;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.jwt.JwtTokenProvider;
import com.yht.exerciseassist.util.SecurityUtil;
import com.yht.exerciseassist.jwt.dto.TokenInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
class MemberServiceTest {
    private static MockedStatic<SecurityUtil> securityUtilMockedStatic;
    MemberService memberService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private MediaService mediaService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @Value("${file.dir}")
    private String fileDir;

    @AfterEach
    public void afterAll() {
        securityUtilMockedStatic.close();
    }

    @BeforeEach
    void setup() {
        memberService = new MemberService(memberRepository, passwordEncoder, authenticationManagerBuilder, jwtTokenProvider, mediaService);
        securityUtilMockedStatic = mockStatic(SecurityUtil.class);
    }

    @Test
    public void signUp() {
        //given
        SignUpRequestDto signUpRequestDto = MemberFactory.createTestSignUpRequestDto();

        Member member = MemberFactory.createTestMember();

        Mockito.when(memberRepository.save(member)).thenReturn(member);
        //when
        ResponseResult response = memberService.join(signUpRequestDto);
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getData()).isEqualTo(member.getUsername());
    }

    @Test
    public void signIn() {
        //given
        SignInRequestDto signInRequestDto = MemberFactory.createTestSignInRequestDto();

        TokenInfo tokenInfo = MemberFactory.createTestTokenInfo();

        Member member = Member.builder()
                .username("member1")
                .email("test@test.com")
                .loginId("testId1")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .role(MemberType.USER)
                .password(passwordEncoder.encode("testPassword1!"))
                .field("서울시")
                .build();

        Mockito.when(memberRepository.findByLoginId(signInRequestDto.getLoginId())).thenReturn(Optional.ofNullable(member));

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

    @Test
    public void deleteMember() throws IOException {
        //given
        Member member = MemberFactory.createTestMember();

        Media media = MediaFactory.createTeatMedia(fileDir + "test1.png");
        media.setMediaIdUsedOnlyTest(1L);

        given(SecurityUtil.getCurrentUsername()).willReturn("username");
        Mockito.when(memberRepository.findByUsername(SecurityUtil.getCurrentUsername())).thenReturn(Optional.ofNullable(member));

        member.ChangeMedia(media);

        //when
        memberService.deleteMember();
        //then
        assertThat(member.getDateTime().getCanceledAt()).isEqualTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        assertThat(member.getMedia()).isEqualTo(null);
    }
}
