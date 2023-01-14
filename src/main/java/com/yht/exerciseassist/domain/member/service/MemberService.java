package com.yht.exerciseassist.domain.member.service;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.MemberType;
import com.yht.exerciseassist.domain.member.dto.SignUpRequestDto;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Optional<Member> findMember = memberRepository.findByLoginId(loginId);
        Member member = findMember.get();
        return createUserDetails(member);
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