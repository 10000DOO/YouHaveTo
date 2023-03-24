package com.yht.exerciseassist.cron.cronService;

import com.yht.exerciseassist.domain.emailCode.repository.EmailCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmailCron {

    private final EmailCodeRepository emailCodeRepository;

    @Scheduled(cron = "0 */1 * * * *")
    public void deleteOldMailCode() {
        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
        emailCodeRepository.deleteByCreatedAtBefore(fiveMinutesAgo);
        log.info("5분 지난 인증 코드 삭제");
    }
}
