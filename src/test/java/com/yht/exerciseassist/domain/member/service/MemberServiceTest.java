package com.yht.exerciseassist.domain.member.service;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.comment.repository.CommentRepository;
import com.yht.exerciseassist.domain.diary.repository.DiaryRepository;
import com.yht.exerciseassist.domain.emailCode.repository.EmailCodeRepository;
import com.yht.exerciseassist.domain.factory.EmailCodeFactory;
import com.yht.exerciseassist.domain.factory.MediaFactory;
import com.yht.exerciseassist.domain.factory.MemberFactory;
import com.yht.exerciseassist.domain.likeCount.repository.LikeCountRepository;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.media.service.MediaService;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.MemberType;
import com.yht.exerciseassist.domain.member.dto.*;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.domain.post.repository.PostRepository;
import com.yht.exerciseassist.jwt.JwtTokenProvider;
import com.yht.exerciseassist.jwt.dto.TokenInfo;
import com.yht.exerciseassist.util.ResponseResult;
import com.yht.exerciseassist.util.SecurityUtil;
import com.yht.exerciseassist.util.TempPassword;
import jakarta.persistence.EntityManager;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    private static MockedStatic<TempPassword> tempPasswordMockedStatic;
    MemberService memberService;
    @Autowired
    EntityManager em;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private EmailCodeRepository emailCodeRepository;
    @MockBean
    private MediaService mediaService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private DiaryRepository diaryRepository;
    @MockBean
    private LikeCountRepository likeCountRepository;
    @MockBean
    private PostRepository postRepository;
    @MockBean
    private CommentRepository commentRepository;
    @Value("${test.address}")
    private String testAddress;

    @AfterEach
    public void afterAll() {
        securityUtilMockedStatic.close();
        tempPasswordMockedStatic.close();
    }

    @BeforeEach
    void setup() {
        memberService = new MemberService(memberRepository, passwordEncoder, emailCodeRepository,
                authenticationManagerBuilder, jwtTokenProvider, mediaService, diaryRepository,
                likeCountRepository, postRepository, commentRepository);
        securityUtilMockedStatic = mockStatic(SecurityUtil.class);
        tempPasswordMockedStatic = mockStatic(TempPassword.class);
    }

    @Test
    public void signUp() {
        //given
        SignUpRequestDto signUpRequestDto = MemberFactory.createTestSignUpRequestDto();
        String code = "123456789ABC";
        Member member = MemberFactory.createTestMember();

        Mockito.when(memberRepository.save(member)).thenReturn(member);
        Mockito.when(emailCodeRepository.findByCode(code)).thenReturn(Optional.of(EmailCodeFactory.createEmailCode()));
        //when
        ResponseResult response = memberService.join(signUpRequestDto, code);
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
    public void deleteMember() {
        //given
        Member member = Member.builder()
                .username("member1")
                .email("test@test.com")
                .loginId("testId1")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .role(MemberType.USER)
                .password(passwordEncoder.encode("testPassword1!"))
                .field("서울시")
                .build();

        Media media = MediaFactory.createTeatMedia();

        given(SecurityUtil.getCurrentUsername()).willReturn("member1");
        Mockito.when(memberRepository.findByNotDeletedUsername(SecurityUtil.getCurrentUsername())).thenReturn(Optional.ofNullable(member));

        em.persist(media);
        Media findMedia = em.find(Media.class, media.getId());
        member.ChangeMedia(findMedia);
        em.persist(member);
        //when
        memberService.deleteMember();
        em.flush();
        em.clear();
        //then
        Member findMember = em.find(Member.class, member.getId());
        assertThat(findMember.getMedia()).isEqualTo(null);
        assertThat(findMember.getDateTime().getCanceledAt()).isNotNull();
    }

    @Test
    public void memberPage() {
        //given
        Member member = MemberFactory.createTestMember();
        String username = "member1";

        Mockito.when(memberRepository.findByNotDeletedUsername(username)).thenReturn(Optional.ofNullable(member));
        given(SecurityUtil.getCurrentUsername()).willReturn("member1");

        ResponseResult<MyMemberPage> mypage = new ResponseResult<>(200, MemberFactory.createMyMemberPage());
        //when
        ResponseResult result = memberService.getMemberPage(username);
        //then
        assertThat(result).isEqualTo(mypage);
    }

    @Test
    public void findId() {
        //given
        String code = "2DG7yGylFhQW";
        Mockito.when(emailCodeRepository.findByCode(code)).thenReturn(Optional.of(EmailCodeFactory.createEmailCode()));
        Mockito.when(memberRepository.findByEmail(EmailCodeFactory.createEmailCode().getEmail())).thenReturn(Optional.ofNullable(MemberFactory.createTestMember()));

        ResponseResult<String> responseResult = new ResponseResult<>(200, "te***d1");
        //when
        ResponseResult<String> result = memberService.findId(code);
        //then
        assertThat(result).isEqualTo(responseResult);
    }

    @Test
    public void findPW() {
        //given
        FindPWDto findPWDto = new FindPWDto("testPassword3!", "test@test.com");
        Mockito.when(memberRepository.findByEmail(findPWDto.getEmail())).thenReturn(Optional.ofNullable(MemberFactory.createTestMember()));

        ResponseResult<String> responseResult = new ResponseResult<>(200, "testPassword3!");
        //when
        ResponseResult<String> result = memberService.findPw(findPWDto);
        //then
        assertThat(result).isEqualTo(responseResult);
    }

    @Test
    public void editMember() throws IOException {
        //given
        EditMemberDto editMemberDto = new EditMemberDto("MANDOO", "testPassword1!", "남양주시", 1L);

        Member testMember = MemberFactory.createTestMember();
        testMember.setMemberIdUsedOnlyTest(1L);
        String fileName = "tuxCoding.jpg";
        MockMultipartFile mediaFile = new MockMultipartFile("files", fileName, "image/jpeg", new FileInputStream(testAddress + "tuxCoding.jpg"));
        List<MultipartFile> files = new ArrayList<>();
        files.add(mediaFile);

        List<Media> media = new ArrayList<>();
        media.add(MediaFactory.createTeatMedia());

        given(SecurityUtil.getCurrentUsername()).willReturn("member1");
        Mockito.when(memberRepository.findByNotDeletedUsername(SecurityUtil.getCurrentUsername())).thenReturn(Optional.ofNullable(testMember));
        Mockito.when(mediaService.uploadMediaToFiles(files)).thenReturn(media);

        ResponseResult<String> responseResult = new ResponseResult<>(200, "MANDOO");
        //when
        ResponseResult<String> result = memberService.editMemberData(editMemberDto, files);
        //then
        assertThat(responseResult).isEqualTo(result);
    }

    @Test
    public void passwordValidation(){
        //given
        PasswordCheckDto passwordCheckDto = new PasswordCheckDto();
        passwordCheckDto.setPassword("testPassword1!");

        Member testMember = Member.builder()
                .username("member1")
                .email("test@test.com")
                .loginId("testId1")
                .dateTime(new DateTime("2023-02-11 11:11", "2023-02-11 11:11", null))
                .role(MemberType.USER)
                .password(passwordEncoder.encode("testPassword1!"))
                .field("서울시")
                .build();

        Mockito.when(memberRepository.findByNotDeletedUsername(SecurityUtil.getCurrentUsername())).thenReturn(Optional.ofNullable(testMember));

        ResponseResult<Long> responseResult = new ResponseResult<>(200, testMember.getId());
        //when
        ResponseResult<Long> result = memberService.passwordValidation(passwordCheckDto);
        //then
        assertThat(responseResult).isEqualTo(result);
    }
}
