package com.yht.exerciseassist.cron.cronService;

import com.yht.exerciseassist.domain.diary.repository.DiaryRepository;
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
public class DiaryCron {

    private final DiaryRepository diaryRepository;

    @Scheduled(cron = "0 0 4 * * *")
    public void deleteOldMailCode() {
        String minusDays = LocalDate.parse(LocalDateTime.now().minusDays(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).toString();
        diaryRepository.deleteByCancealedAt(minusDays);
        log.info("30일 지난 다이어리 삭제");
    }
}
