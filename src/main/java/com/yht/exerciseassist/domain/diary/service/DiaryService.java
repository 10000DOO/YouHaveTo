package com.yht.exerciseassist.domain.diary.service;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.diary.Diary;
import com.yht.exerciseassist.domain.diary.ExerciseInfo;
import com.yht.exerciseassist.domain.diary.dto.ExerciseInfoDto;
import com.yht.exerciseassist.domain.diary.dto.WriteDiaryDto;
import com.yht.exerciseassist.domain.diary.repository.DiaryRepository;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.media.service.MediaService;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.jwt.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;
    private final MediaService mediaService;

//    public ResponseResult getDiaryList(String date) {
//
//        List<Diary> findDiaries = diaryRepository.findDiariesByUsername(SecurityUtil.getCurrentMemberId(), date);
//        if (findDiaries == null || findDiaries.isEmpty()) {
//            throw new IllegalArgumentException("존재하지 않는 다이어리입니다.");
//        } else {
//            findDiaries.stream().map(diary -> new DiaryListDto)
//        }
//    }

    public ResponseEntity saveDiary(WriteDiaryDto writeDiaryDto, List<MultipartFile> files) throws IOException {
        Member findMember = memberRepository.findByUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        List<ExerciseInfoDto> exerciseInfoDto = writeDiaryDto.getExerciseInfo();
        List<ExerciseInfo> exInfo = exerciseInfoDto.stream()
                .map(e -> ExerciseInfo.builder().exerciseName(e.getExerciseName()).bodyPart(e.getBodyPart())
                        .exSetCount(e.getExSetCount()).reps(e.getReps()).cardio(e.isCardio())
                        .cardioTime(e.getCardioTime()).finished(e.isFinished()).build())
                .collect(Collectors.toList());

        if (files != null && !(files.isEmpty())) {
            List<Media> mediaList = mediaService.uploadImageToFileSystem(files);

            Diary diary = Diary.builder()
                    .member(findMember)
                    .exerciseDate(writeDiaryDto.getExerciseDate())
                    .review(writeDiaryDto.getReview())
                    .exerciseInfo(exInfo)
                    .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                    .build();

            diary.linkToMedia(mediaList);

            diaryRepository.save(diary);

            log.info("사용자명 : " + findMember.getUsername() + " 다이어리 등록 완료");
            ResponseResult responseResult = new ResponseResult(HttpStatus.CREATED.value(), writeDiaryDto.getExerciseDate());
            return ResponseEntity.status(HttpStatus.CREATED).body(responseResult);
        } else {
            Diary diary = Diary.builder()
                    .member(findMember)
                    .exerciseDate(writeDiaryDto.getExerciseDate())
                    .review(writeDiaryDto.getReview())
                    .exerciseInfo(exInfo)
                    .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                    .build();

            diaryRepository.save(diary);

            log.info("사용자명 : " + findMember.getUsername() + " 다이어리 등록 완료");
            ResponseResult responseResult = new ResponseResult(HttpStatus.CREATED.value(), writeDiaryDto.getExerciseDate());
            return ResponseEntity.status(HttpStatus.CREATED).body(responseResult);
        }
    }
}
