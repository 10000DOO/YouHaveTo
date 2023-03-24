package com.yht.exerciseassist.cron.cronService;

import com.yht.exerciseassist.domain.accuse.repository.AccuseRepository;
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
public class AccuseCron {

    private final AccuseRepository accuseRepository;

    //@Scheduled(cron = "0 0 4 * * *")
    @Scheduled(cron = "0 */1 * * * *")
    public void deleteOldMailCode() {
        String minusDays = LocalDate.parse(LocalDateTime.now().minusDays(14).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).toString();
        accuseRepository.deleteByCreatedBefore(minusDays);
        log.info("14일 지난 신고 삭제");
    }
}
