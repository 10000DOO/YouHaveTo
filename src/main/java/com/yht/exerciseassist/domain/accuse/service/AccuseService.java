package com.yht.exerciseassist.domain.accuse.service;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.accuse.Accuse;
import com.yht.exerciseassist.domain.accuse.dto.AccuseReq;
import com.yht.exerciseassist.domain.accuse.repository.AccuseRepository;
import com.yht.exerciseassist.domain.post.Post;
import com.yht.exerciseassist.domain.post.repository.PostRepository;
import com.yht.exerciseassist.exception.error.ErrorCode;
import com.yht.exerciseassist.util.ResponseResult;
import com.yht.exerciseassist.util.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccuseService {

    private final AccuseRepository accuseRepository;
    private final PostRepository postRepository;

    public ResponseResult<Long> saveAccuse(Long postId, AccuseReq accuseReq) {
        String memberRole = SecurityUtil.getMemberRole();
        Post post = postRepository.findByIdWithRole(postId, memberRole)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_POST.getMessage()));

        Accuse accuse = Accuse.builder()
                .accuseType(accuseReq.getAccuseType())
                .content(accuseReq.getContent())
                .post(post)
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                .build();

        accuseRepository.save(accuse);
        log.info("{}유저 {} 게시글 신고 완료", SecurityUtil.getCurrentUsername(), post.getId());
        return new ResponseResult<>(HttpStatus.CREATED.value(), accuse.getId());
    }

    public ResponseResult<Long> deleteAccuse(Long accuseId) {

        Accuse findAccuse = accuseRepository.findByNotDeletedId(accuseId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_ACCUSE.getMessage()));

        findAccuse.getDateTime().canceledAtUpdate();
        log.info("{}유저 {} 신고 삭제 완료", SecurityUtil.getCurrentUsername(), findAccuse.getId());
        return new ResponseResult<>(HttpStatus.OK.value(), findAccuse.getId());
    }
}
