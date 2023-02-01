package com.yht.exerciseassist.domain.diary.service;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.diary.Diary;
import com.yht.exerciseassist.domain.diary.dto.ExerciseInfoDto;
import com.yht.exerciseassist.domain.diary.dto.WriteDiaryDto;
import com.yht.exerciseassist.domain.diary.repository.DiaryRepository;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.jwt.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;

    public ResponseResult saveDiary(WriteDiaryDto writeDiaryDto) {
        Member findMember = memberRepository.findByUsername(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        List<ExerciseInfoDto> exerciseInfoDto = writeDiaryDto.getExerciseInfo();
        for (ExerciseInfoDto infoDto : exerciseInfoDto) {
            Diary diary = Diary.builder()
                    .member(findMember)
                    .exerciseName(infoDto.getExerciseName())
                    .reps(infoDto.getReps())
                    .exSetCount(infoDto.getExSetCount())
                    .review(writeDiaryDto.getReview())
                    .cardio(infoDto.isCardio())
                    .cardioTime(infoDto.getCardioTime())
                    .exerciseDate(writeDiaryDto.getExerciseDate())
                    .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                    .build();

            diaryRepository.save(diary);
        }

        log.info("사용자명 : " + findMember.getUsername() + " 다이어리 등록 완료");
        return new ResponseResult(HttpStatus.CREATED.value(), writeDiaryDto.getExerciseDate());
    }
}
