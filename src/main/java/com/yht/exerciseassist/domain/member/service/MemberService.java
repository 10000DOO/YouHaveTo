package com.yht.exerciseassist.domain.member.service;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.MemberType;
import com.yht.exerciseassist.domain.member.dto.SignUpRequestDto;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.exceoption.error.AuthenticationException;
import com.yht.exerciseassist.jwt.JwtTokenProvider;
import com.yht.exerciseassist.jwt.dto.TokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
import org.springframework.util.StringUtils;

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
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public ResponseResult join(SignUpRequestDto signUpRequestDto){
        String result = validateDuplicateMember(signUpRequestDto);

        if (StringUtils.hasText(result)){
            throw new IllegalArgumentException(result);
        }else {
            Member member = Member.builder()
                    .username(signUpRequestDto.getUsername())
                    .email(signUpRequestDto.getEmail())
                    .loginId(signUpRequestDto.getLoginId())
                    .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                    .field(signUpRequestDto.getField())
                    .role(MemberType.USER)
                    .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            null))
                    .build();

            memberRepository.save(member);
            log.info(member.getUsername()+" 회원가입 완료");

            return new ResponseResult(HttpStatus.CREATED.value(), member.getUsername());
        }
    }

    private String validateDuplicateMember(SignUpRequestDto signUpRequestDto) {
        Optional<Member> findIdMember = memberRepository.findByLoginId(signUpRequestDto.getLoginId());
        Optional<Member> findEmailMember = memberRepository.findByEmail(signUpRequestDto.getEmail());
        Optional<Member> findUnameMember = memberRepository.findByUsername(signUpRequestDto.getUsername());
        String errorMessage = "";
        if (findIdMember.isPresent()){
            errorMessage += "이미 존재하는 아이디입니다. ";
        }
        if (findEmailMember.isPresent()){
            errorMessage += "이미 존재하는 이메일입니다. ";
        }
        if (findUnameMember.isPresent()){
            errorMessage += "이미 존재하는 이름입니다. ";
        }
        return errorMessage;
    }

    public ResponseResult signIn(String loginId, String password){

        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = null;
        try {
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException){
                throw new BadCredentialsException("비밀번호가 틀렸습니다. 다시 시도해주세요.");
            } else if (e instanceof InternalAuthenticationServiceException){
                throw new InternalAuthenticationServiceException("아이디가 틀렸습니다. 다시 시도해주세요.");
            }
        }

        if (authentication == null){
            throw new AuthenticationException("로그인 실패입니다 다시 시도해주세요.");
        } else {
            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
            log.info(authentication.getName()+" 로그인");
            Member member = memberRepository.findByLoginId(loginId).get();
            member.setRefreshToken(tokenInfo.getRefreshToken());
            return new ResponseResult(HttpStatus.OK.value(), tokenInfo);
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
}