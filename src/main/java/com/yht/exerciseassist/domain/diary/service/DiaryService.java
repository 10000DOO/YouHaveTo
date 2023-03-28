package com.yht.exerciseassist.domain.diary.service;

import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.diary.Diary;
import com.yht.exerciseassist.domain.diary.ExerciseInfo;
import com.yht.exerciseassist.domain.diary.dto.*;
import com.yht.exerciseassist.domain.diary.repository.DiaryRepository;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.media.service.MediaService;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.exception.error.ErrorCode;
import com.yht.exerciseassist.util.ResponseResult;
import com.yht.exerciseassist.util.SecurityUtil;
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

    public ResponseResult<DiaryListDto> getDiaryList(String date) {
        List<Diary> findDiaries = diaryRepository.findDiariesByUsername(SecurityUtil.getCurrentUsername(), date);
        if (findDiaries == null || findDiaries.isEmpty()) {
            throw new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_DIARY.getMessage());
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

            log.info(date + " : 다이어리 목록 조회 성공");
            return new ResponseResult<>(HttpStatus.OK.value(), diaryListDto);
        }
    }

    public ResponseResult<String> saveDiary(WriteDiaryDto writeDiaryDto, List<MultipartFile> files) throws IOException {
        Member findMember = memberRepository.findByNotDeletedUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_MEMBER.getMessage()));

        List<ExerciseInfo> exInfo = getExInfo(writeDiaryDto);

        Diary diary = Diary.builder()
                .member(findMember)
                .exerciseDate(writeDiaryDto.getExerciseDate())
                .review(writeDiaryDto.getReview())
                .exerciseInfo(exInfo)
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                .build();

        if (files != null && !(files.isEmpty())) {
            List<Media> mediaList = mediaService.uploadMediaToFileSystem(files);
            diary.linkToMedia(mediaList);
        }
        diaryRepository.save(diary);

        log.info("사용자명 : " + findMember.getUsername() + " 다이어리 등록 완료");
        return new ResponseResult<>(HttpStatus.CREATED.value(), writeDiaryDto.getExerciseDate());
    }

    public ResponseResult<DiaryDetailDto> getDiaryDetail(String date) {
        Diary findDiary = diaryRepository.findDiaryDetailsByUsername(SecurityUtil.getCurrentUsername(), date)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_DIARY.getMessage()));

        List<ExerciseInfoResDto> exInfoDto = getExInfoResDto(findDiary);

        List<String> mediaId = getMediaList(findDiary);

        DiaryDetailDto diaryDetailDto = DiaryDetailDto.builder()
                .exerciseDate(findDiary.getExerciseDate())
                .review(findDiary.getReview())
                .exerciseInfo(exInfoDto)
                .createdAt(findDiary.getDateTime().getCreatedAt())
                .mediaList(mediaId)
                .build();


        log.info(date + "다이어리 상세 조회 성공");
        return new ResponseResult<>(HttpStatus.OK.value(), diaryDetailDto);
    }

    public ResponseResult<DiaryEditData> getDiaryEditData(Long diaryId) {
        Diary diary = diaryRepository.findByNotDeleteId(diaryId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_DIARY.getMessage()));

        List<ExerciseInfoResDto> exInfoDto = getExInfoResDto(diary);

        List<String> mediaList = getMediaList(diary);

        DiaryEditData diaryEditData = DiaryEditData.builder()
                .review(diary.getReview())
                .exerciseInfo(exInfoDto)
                .mediaList(mediaList)
                .build();

        log.info("사용자명 : " + SecurityUtil.getCurrentUsername() + " 다이어리 수정 데이터 조회 성공");
        return new ResponseResult<>(HttpStatus.OK.value(), diaryEditData);
    }

    public ResponseResult<String> editDiary(WriteDiaryDto writeDiaryDto, List<MultipartFile> files, Long diaryId) throws IOException {
        Diary diaryById = diaryRepository.findByNotDeleteId(diaryId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_DIARY.getMessage()));

        List<ExerciseInfo> exInfo = getExInfo(writeDiaryDto);

        diaryById.editDiary(writeDiaryDto.getExerciseDate(), writeDiaryDto.getReview(), exInfo);
        diaryById.getDateTime().updatedAtUpdate();

        if (files != null && !(files.isEmpty())) {
            mediaService.deleteDiaryMedia(diaryId);
            List<Media> mediaList = mediaService.uploadMediaToFileSystem(files);
            diaryById.linkToMedia(mediaList);
        }
        diaryRepository.save(diaryById);

        log.info("사용자명 : " + SecurityUtil.getCurrentUsername() + " 다이어리 수정 완료");
        return new ResponseResult<>(HttpStatus.OK.value(), diaryById.getExerciseDate());
    }

    public ResponseResult<Long> deleteDiary(Long diaryId) throws IOException {
        Diary diaryById = diaryRepository.findByNotDeleteId(diaryId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND_EXCEPTION_DIARY.getMessage()));

        mediaService.deleteDiaryMedia(diaryById.getId());
        diaryById.getDateTime().canceledAtUpdate();

        log.info("username : {}, {}번 다이어리 삭제 완료", SecurityUtil.getCurrentUsername(), diaryById.getId());
        return new ResponseResult<>(HttpStatus.OK.value(), diaryById.getId());
    }

    private List<String> getMediaList(Diary diary) {
        List<String> mediaList = new ArrayList<>();
        for (Media media : diary.getMediaList()) {
            mediaList.add(baseUrl + "/media/" + media.getId());
        }
        return mediaList;
    }

    private List<ExerciseInfoResDto> getExInfoResDto(Diary diary) {
        return diary.getExerciseInfo().stream()
                .map(e -> ExerciseInfoResDto.builder().exerciseName(e.getExerciseName()).bodyPart(e.getBodyPart().getMessage())
                        .exSetCount(e.getExSetCount()).cardio(e.isCardio()).reps(e.getReps())
                        .cardioTime(e.getCardioTime()).finished(e.isFinished()).build())
                .collect(Collectors.toList());
    }

    private List<ExerciseInfo> getExInfo(WriteDiaryDto writeDiaryDto) {
        return writeDiaryDto.getExerciseInfo().stream()
                .map(e -> ExerciseInfo.builder().exerciseName(e.getExerciseName()).bodyPart(e.getBodyPart())
                        .exSetCount(e.getExSetCount()).reps(e.getReps()).cardio(e.isCardio())
                        .cardioTime(e.getCardioTime()).finished(e.isFinished()).build())
                .collect(Collectors.toList());
    }
}
