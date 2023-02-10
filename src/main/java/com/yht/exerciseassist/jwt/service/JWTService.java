package com.yht.exerciseassist.jwt.service;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.jwt.JwtTokenProvider;
import com.yht.exerciseassist.jwt.dto.TokenInfo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JWTService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public ResponseResult refreshToken(String inputToken) {
        Base64.Decoder decoder = Base64.getDecoder();
        String payload = new String(decoder.decode(inputToken.split("\\.")[1]));
        long time = new Date().getTime();
        long expTime = Long.parseLong(payload.substring(7, payload.length() - 1));

        if ((time / 1000) < expTime) {
            Optional<Member> byRefreshToken = memberRepository.findByRefreshToken(inputToken);
            if (byRefreshToken.isPresent()) {
                Member findMember = byRefreshToken.get();
                String token = findMember.getRefreshToken();
                if (token.equals(inputToken)) {
                    long now = (new Date()).getTime();
                    // Access Token 생성
                    Date accessTokenExpiresIn = new Date(now + 30 * 60 * 1000);
                    String accessToken = Jwts.builder()
                            .setSubject(findMember.getUsername())
                            .claim("auth", "ROLE_" + findMember.getRole())
                            .setExpiration(accessTokenExpiresIn)
                            .signWith(jwtTokenProvider.getKey(), SignatureAlgorithm.HS512)
                            .compact();

                    // Refresh Token 생성
                    String refreshToken = Jwts.builder()
                            .setExpiration(new Date(now + 14 * 24 * 60 * 60 * 1000))
                            .signWith(jwtTokenProvider.getKey(), SignatureAlgorithm.HS512)
                            .compact();
                    TokenInfo tokenInfo = TokenInfo.builder()
                            .grantType("Bearer")
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                    findMember.setRefreshToken(tokenInfo.getRefreshToken());
                    log.info("토큰이 재발급 되었습니다.");
                    return new ResponseResult(HttpStatus.OK.value(), tokenInfo);
                } else {
                    return new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "잘못된 토큰입니다. 토큰 재발급이 불가능하니 " +
                            "다시 로그인 부탁드립니다.");
                }
            } else {
                return new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "존재하지 않는 토큰입니다. 토큰 재발급이 불가능하니 " +
                        "다시 로그인 부탁드립니다.");
            }
        } else {
            return new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "토큰이 만료 되었습니다. 다시 로그인 해주세요.");
        }
    }
}
