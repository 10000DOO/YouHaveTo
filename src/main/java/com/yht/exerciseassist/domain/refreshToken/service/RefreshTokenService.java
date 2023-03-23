package com.yht.exerciseassist.domain.refreshToken.service;

import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.domain.refreshToken.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 0 4 * * *")
    public void deleteOldMemberData() {
        String minusDays = LocalDate.parse(LocalDateTime.now().minusDays(14).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).toString();
        List<Long> findTokenId = refreshTokenRepository.findByUpdatedAtBefore(minusDays);
        memberRepository.updateByRefreshToken(findTokenId);
        refreshTokenRepository.deleteByUpdatedAtBefore(minusDays);
        log.info("14일 지난 refreshToken 삭제");
    }
}
