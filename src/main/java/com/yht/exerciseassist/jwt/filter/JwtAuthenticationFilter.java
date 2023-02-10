package com.yht.exerciseassist.jwt.filter;

import com.yht.exerciseassist.exceoption.error.jwt.EmptyJWTTokenExcep;
import com.yht.exerciseassist.exceoption.error.jwt.ExpiredJWTTokenExcep;
import com.yht.exerciseassist.exceoption.error.jwt.InvalidJWTTokenExcep;
import com.yht.exerciseassist.exceoption.error.jwt.UnsupportedJWTTokenExcep;
import com.yht.exerciseassist.jwt.JwtTokenProvider;
import com.yht.exerciseassist.jwt.JwtTokenResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenResolver jwtTokenResolver;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 1. Request Header 에서 JWT 토큰 추출
        String token = jwtTokenResolver.resolveToken((HttpServletRequest) request);

        if (token == null) {
            //회원가입 로그인 시
        } else {
            // 2. validateToken 으로 토큰 유효성 검사
            switch (jwtTokenProvider.validateToken(token)) {
                case VALID -> {
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                case EXPIRED -> {
                    throw new ExpiredJWTTokenExcep("만료된 토큰입니다.");
                }
                case INVALID -> {
                    throw new InvalidJWTTokenExcep("유효하지 않은 토큰입니다.");
                }
                case UNSUPPORTED -> {
                    throw new UnsupportedJWTTokenExcep("지원되지 않는 토큰입니다");
                }
                case EMPTY -> {
                    throw new EmptyJWTTokenExcep("claims 내용이 빈 토큰입니다.");
                }
            }
        }
        chain.doFilter(request, response);
    }
}