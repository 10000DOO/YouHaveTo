package com.yht.exerciseassist.domain.diary.service;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.diary.Diary;
import com.yht.exerciseassist.domain.diary.ExerciseInfo;
import com.yht.exerciseassist.domain.diary.dto.*;
import com.yht.exerciseassist.domain.diary.repository.DiaryRepository;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.media.service.MediaService;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.jwt.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final MemberRepository memberRepository;
    private final MediaService mediaService;
    @Value("${base.url}")
    private String baseUrl;

    public ResponseResult getDiaryList(String date) {

        List<Diary> findDiaries = diaryRepository.findDiariesByUsername(SecurityUtil.getCurrentUsername(), date);
        if (findDiaries == null || findDiaries.isEmpty()) {
            throw new EntityNotFoundException("존재하지 않는 다이어리입니다.");
        } else {
            double monthlyTotal = 0;
            double monthlyFinished = 0;
            List<Calender> calender = new ArrayList<>();

            for (Diary findDiary : findDiaries) {
                int exSize = findDiary.getExerciseInfo().size();
                monthlyTotal += exSize;
                double dailyTotal = exSize;
                double dailyFinished = 0;

                for (ExerciseInfo exerciseInfo : findDiary.getExerciseInfo()) {
                    if (exerciseInfo.isFinished()) {
                        monthlyFinished++;
                        dailyFinished++;
                    }
                }

                int dailyPercentage = (int) Math.round(dailyFinished / dailyTotal * 100);
                Calender cal = new Calender(findDiary.getExerciseDate(), dailyPercentage);
                calender.add(cal);
            }
            int monthlyPercentage = (int) Math.round(monthlyFinished / monthlyTotal * 100);
            DiaryListDto diaryListDto = new DiaryListDto(calender, monthlyPercentage);

            ResponseResult responseResult = new ResponseResult(HttpStatus.OK.value(), diaryListDto);

            log.info(date + " : 다이어리 목록 조회 성공");

            return responseResult;
        }
    }

    public ResponseResult saveDiary(WriteDiaryDto writeDiaryDto, List<MultipartFile> files) throws IOException {
        Member findMember = memberRepository.findByUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        List<ExerciseInfoDto> exerciseInfoDto = writeDiaryDto.getExerciseInfo();
        List<ExerciseInfo> exInfo = exerciseInfoDto.stream()
                .map(e -> ExerciseInfo.builder().exerciseName(e.getExerciseName()).bodyPart(e.getBodyPart())
                        .exSetCount(e.getExSetCount()).reps(e.getReps()).cardio(e.isCardio())
                        .cardioTime(e.getCardioTime()).finished(e.isFinished()).build())
                .collect(Collectors.toList());

        Diary diary = Diary.builder()
                .member(findMember)
                .exerciseDate(writeDiaryDto.getExerciseDate())
                .review(writeDiaryDto.getReview())
                .exerciseInfo(exInfo)
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                .build();

        if (files != null && !(files.isEmpty())) {
            List<Media> mediaList = mediaService.uploadImageToFileSystem(files);
            diary.linkToMedia(mediaList);
        }
        diaryRepository.save(diary);

        log.info("사용자명 : " + findMember.getUsername() + " 다이어리 등록 완료");
        ResponseResult responseResult = new ResponseResult(HttpStatus.CREATED.value(), writeDiaryDto.getExerciseDate());
        return responseResult;
    }

    public ResponseResult getdiaryDetail(String date) {
        Optional<Diary> diaryDetails = diaryRepository.findDiaryDetailsByUsername(SecurityUtil.getCurrentUsername(), date);
        Diary findDiary = diaryDetails.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 다이어리입니다."));

        List<ExerciseInfoDto> exInfoDto = findDiary.getExerciseInfo().stream()
                .map(e -> ExerciseInfoDto.builder().exerciseName(e.getExerciseName()).bodyPart(e.getBodyPart())
                        .exSetCount(e.getExSetCount()).cardio(e.isCardio()).reps(e.getReps())
                        .cardioTime(e.getCardioTime()).finished(e.isFinished()).build())
                .collect(Collectors.toList());

        List<String> mediaId = new ArrayList<>();

        for (Media media : findDiary.getMediaList()) {
            mediaId.add(baseUrl + "/media/" + media.getId());
        }

        DiaryDetailDto diaryDetailDto = DiaryDetailDto.builder()
                .exerciseDate(findDiary.getExerciseDate())
                .review(findDiary.getReview())
                .exerciseInfo(exInfoDto)
                .dateTime(findDiary.getDateTime())
                .mediaList(mediaId)
                .build();


        log.info(date + "다이어리 상세 조회 성공");
        return new ResponseResult(HttpStatus.OK.value(), diaryDetailDto);
    }

    public ResponseResult editDiary(WriteDiaryDto writeDiaryDto, List<MultipartFile> files, Long id) throws IOException {
        Member findMember = memberRepository.findByUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Diary diaryById = diaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 다이어리 입니다."));

        List<ExerciseInfoDto> exerciseInfoDto = writeDiaryDto.getExerciseInfo();
        List<ExerciseInfo> exInfo = exerciseInfoDto.stream()
                .map(e -> ExerciseInfo.builder().exerciseName(e.getExerciseName()).bodyPart(e.getBodyPart())
                        .exSetCount(e.getExSetCount()).reps(e.getReps()).cardio(e.isCardio())
                        .cardioTime(e.getCardioTime()).finished(e.isFinished()).build())
                .collect(Collectors.toList());

        diaryById.editDiary(writeDiaryDto.getExerciseDate(), writeDiaryDto.getReview(), exInfo);
        diaryById.getDateTime().updatedAtUpdate();

        if (files != null && !(files.isEmpty())) {
            mediaService.deleteFile(id);
            List<Media> mediaList = mediaService.uploadImageToFileSystem(files);
            diaryById.linkToMedia(mediaList);
        }
        diaryRepository.save(diaryById);

        log.info("사용자명 : " + findMember.getUsername() + " 다이어리 수정 완료");
        ResponseResult responseResult = new ResponseResult(HttpStatus.CREATED.value(), writeDiaryDto.getExerciseDate());
        return responseResult;
    }
}
