package com.yht.exerciseassist.domain.member.service;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.emailCode.EmailCode;
import com.yht.exerciseassist.domain.emailCode.repository.EmailCodeRepository;
import com.yht.exerciseassist.domain.media.service.MediaService;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.MemberType;
import com.yht.exerciseassist.domain.member.dto.MyMemberPage;
import com.yht.exerciseassist.domain.member.dto.OtherMemberPage;
import com.yht.exerciseassist.domain.member.dto.SignUpRequestDto;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.domain.refreshToken.RefreshToken;
import com.yht.exerciseassist.exception.error.AuthenticationException;
import com.yht.exerciseassist.exception.error.ErrorCode;
import com.yht.exerciseassist.jwt.JwtTokenProvider;
import com.yht.exerciseassist.jwt.dto.TokenInfo;
import com.yht.exerciseassist.util.ResponseResult;
import com.yht.exerciseassist.util.SecurityUtil;
import com.yht.exerciseassist.util.TempPassword;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailCodeRepository emailCodeRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final MediaService mediaService;
    @Value("${base.url}")
    private String baseUrl;

    public ResponseResult<String> join(SignUpRequestDto signUpRequestDto, String code) {
        EmailCode emailCode = emailCodeRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.WRONG_EMAIL_CODE.getMessage()));
        Member member = Member.builder()
                .username(signUpRequestDto.getUsername())
                .email(emailCode.getEmail())
                .loginId(signUpRequestDto.getLoginId())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .field(signUpRequestDto.getField())
                .role(MemberType.USER)
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                .build();

        memberRepository.save(member);
        log.info(member.getUsername() + " 회원가입 완료");

        return new ResponseResult<>(HttpStatus.CREATED.value(), member.getUsername());
    }

    public ResponseResult<TokenInfo> signIn(String loginId, String password) {

        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = null;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                throw new BadCredentialsException(ErrorCode.BAD_CREDENTIALS_EXCEPTION.getMessage());
            } else if (e instanceof InternalAuthenticationServiceException) {
                throw new InternalAuthenticationServiceException(ErrorCode.INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION.getMessage());
            }
        }

        if (authentication == null) {
            throw new AuthenticationException(ErrorCode.AUTHENTICATION_EXCEPTION.getMessage());
        } else {
            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
            log.info(authentication.getName() + " 로그인");
            Member member = memberRepository.findByLoginId(loginId).get();

            RefreshToken refreshToken = Optional.ofNullable(member.getRefreshToken())
                    .orElse(new RefreshToken(tokenInfo.getRefreshToken()));
            refreshToken.updateRefreshToken(tokenInfo.getRefreshToken());
            member.updateRefreshToken(refreshToken);

            return new ResponseResult<>(HttpStatus.OK.value(), tokenInfo);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Optional<Member> findMember = memberRepository.findByLoginId(loginId);
        return findMember.map(this::createUserDetails).orElseGet(() -> createUserDetails(null));
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Member member) {
        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    public ResponseResult<Long> deleteMember() throws IOException {
        Member member = memberRepository.findByUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_MEMBER.getMessage()));

        Long mediaId = null;

        try {
            mediaId = member.getMedia().getId();
        } catch (NullPointerException e) {
            log.info("mediaId 없음");
        }

        if (mediaId != null) {
            member.ChangeMedia(null);
            mediaService.deleteProfileImage(mediaId);
        }

        member.getDateTime().canceledAtUpdate();

        log.info("username : {}, {}번 유저 삭제 완료", SecurityUtil.getCurrentUsername(), member.getId());
        return new ResponseResult<>(HttpStatus.OK.value(), member.getId());
    }

    public ResponseResult getMemberPage(String username) {
        Member findMember = memberRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_MEMBER.getMessage()));

        String profileImage;
        try {
            profileImage = baseUrl + "/media/" + findMember.getMedia().getId();
        } catch (NullPointerException e) {
            profileImage = null;
        }

        if (username.equals(SecurityUtil.getCurrentUsername())) {
            MyMemberPage myMemberPage = MyMemberPage.builder()
                    .username(findMember.getUsername())
                    .email(findMember.getEmail())
                    .field(findMember.getField())
                    .createdAt(findMember.getDateTime().getCreatedAt())
                    .profileImage(profileImage)
                    .postCount(findMember.getPosts().size())
                    .build();

            return new ResponseResult<>(HttpStatus.OK.value(), myMemberPage);
        } else {
            OtherMemberPage otherMemberPage = OtherMemberPage.builder()
                    .username(findMember.getUsername())
                    .field(findMember.getField())
                    .createdAt(findMember.getDateTime().getCreatedAt())
                    .profileImage(profileImage)
                    .postCount(findMember.getPosts().size())
                    .build();

            return new ResponseResult<>(HttpStatus.OK.value(), otherMemberPage);
        }
    }

    public ResponseResult<String> findId(String code) {
        Optional<EmailCode> optionalEmailCode = emailCodeRepository.findByCode(code);

        if (optionalEmailCode.isPresent()) {
            Member findMember = memberRepository.findByEmail(optionalEmailCode.get().getEmail())
                    .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_MEMBER.getMessage()));
            String loginId = findMember.getLoginId();
            String encodedId = loginId.substring(0, 2) + "***" + loginId.substring(5);
            log.info("{} 아이디 찾기 성공", findMember.getUsername());
            return new ResponseResult<>(HttpStatus.OK.value(), encodedId);
        } else {
            throw new IllegalArgumentException(ErrorCode.WRONG_EMAIL_CODE.getMessage());
        }
    }

    public ResponseResult<String> findPw(String code) {
        Optional<EmailCode> optionalEmailCode = emailCodeRepository.findByCode(code);

        if (optionalEmailCode.isPresent()) {
            Member findMember = memberRepository.findByEmail(optionalEmailCode.get().getEmail())
                    .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_MEMBER.getMessage()));
            String tempPassword = TempPassword.tempPassword(10);
            findMember.changeMemberData(findMember.getUsername(), findMember.getEmail(), passwordEncoder.encode(tempPassword), findMember.getField());
            log.info("{} 임시 비밀번호 생성", findMember.getUsername());
            return new ResponseResult<>(HttpStatus.OK.value(), tempPassword);
        } else {
            throw new IllegalArgumentException(ErrorCode.WRONG_EMAIL_CODE.getMessage());
        }
    }

    @Scheduled(cron = "0 0 4 * * *")
    public void deleteOldMemberData() {
        String minusMonths = LocalDate.parse(LocalDateTime.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).toString();
        memberRepository.deleteByCreatedAtBefore(minusMonths);
        log.info("탈퇴한지 30일 지난 멤버 데이터 삭제");
    }
}
