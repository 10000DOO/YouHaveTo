package com.yht.exerciseassist.jwt.service;

import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.exception.error.ErrorCode;
import com.yht.exerciseassist.jwt.JwtTokenProvider;
import com.yht.exerciseassist.jwt.dto.TokenInfo;
import com.yht.exerciseassist.util.ResponseResult;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Date;
import java.util.Objects;
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
                String token = findMember.getRefreshToken().getRefreshToken();
                if (Objects.equals(token, inputToken)) {
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

                    findMember.getRefreshToken().updateRefreshToken(tokenInfo.getRefreshToken());
                    log.info("토큰이 재발급 되었습니다.");
                    return new ResponseResult(HttpStatus.OK.value(), tokenInfo);
                } else {
                    return new ResponseResult<>(HttpStatus.UNAUTHORIZED.value(), ErrorCode.WRONG_TOKEN.getMessage());
                }
            } else {
                return new ResponseResult<>(HttpStatus.UNAUTHORIZED.value(), ErrorCode.NO_EXIST_TOKEN.getMessage());
            }
        } else {
            return new ResponseResult<>(HttpStatus.UNAUTHORIZED.value(), ErrorCode.EXPIRED_TOKEN.getMessage());
        }
    }
}
