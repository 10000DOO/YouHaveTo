package com.yht.exerciseassist.cron.cronService;


import com.yht.exerciseassist.domain.comment.repository.CommentRepository;
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
public class CommentCron {

    private final CommentRepository commentRepository;

    @Scheduled(cron = "0 45 3 * * *")
    public void deleteOldMemberData() {
        String minusDays = LocalDate.parse(LocalDateTime.now().minusDays(30).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).toString();
        commentRepository.deleteByCancealedAt(minusDays);
        log.info("30일 지난 댓글 삭제");
    }
}
