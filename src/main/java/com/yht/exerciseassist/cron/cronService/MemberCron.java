package com.yht.exerciseassist.cron.cronService;

import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberCron {

    private final MemberRepository memberRepository;

    @Scheduled(cron = "0 5 4 * * *")
    public void deleteOldMemberData() {
        String minusDays = LocalDate.parse(LocalDateTime.now().minusDays(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).toString();
        memberRepository.deleteByCancealedAt(minusDays);
        log.info("탈퇴한지 30일 지난 멤버 데이터 삭제");
    }
}
