package com.yht.exerciseassist.domain.diary.service;

import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.DateTime;
import com.yht.exerciseassist.domain.diary.BodyPart;
import com.yht.exerciseassist.domain.diary.Diary;
import com.yht.exerciseassist.domain.diary.ExerciseInfo;
import com.yht.exerciseassist.domain.diary.dto.ExerciseInfoDto;
import com.yht.exerciseassist.domain.diary.dto.WriteDiaryDto;
import com.yht.exerciseassist.domain.diary.repository.DiaryRepository;
import com.yht.exerciseassist.domain.media.service.MediaService;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.MemberType;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.jwt.SecurityUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@Transactional
@Rollback
class DiaryServiceTest {

    private static MockedStatic<SecurityUtil> securityUtilMockedStatic;
    DiaryService diaryService;
    @MockBean
    private DiaryRepository diaryRepository;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private MediaService mediaService;

    @AfterAll
    public static void afterAll() {
        securityUtilMockedStatic.close();
    }

    @BeforeEach
    void setUp() {
        diaryService = new DiaryService(diaryRepository, memberRepository, mediaService);
        securityUtilMockedStatic = mockStatic(SecurityUtil.class);
    }

    @Test
    public void saveDiary() throws IOException {
        //given
        ExerciseInfoDto exerciseInfoDto = new ExerciseInfoDto();
        exerciseInfoDto.setExerciseName("pushUp");
        exerciseInfoDto.setReps(10);
        exerciseInfoDto.setCardio(true);
        exerciseInfoDto.setExSetCount(10);
        exerciseInfoDto.setCardioTime(30);
        exerciseInfoDto.setBodyPart(BodyPart.TRICEP);
        exerciseInfoDto.setFinished(true);

        List<ExerciseInfoDto> exerciseInfoDtoList = new ArrayList<>();
        exerciseInfoDtoList.add(exerciseInfoDto);

        WriteDiaryDto writeDiaryDto = new WriteDiaryDto();
        writeDiaryDto.setExerciseInfo(exerciseInfoDtoList);
        writeDiaryDto.setReview("오늘 운동 끝");
        writeDiaryDto.setExerciseDate("2023-01-30");

        Member member = Member.builder()
                .username("username")
                .email("test@test.com")
                .loginId("testId3")
                .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                .role(MemberType.USER)
                .password("testPassword3!")
                .field("서울시")
                .build();

        given(SecurityUtil.getCurrentUsername()).willReturn("username");
        Mockito.when(memberRepository.findByUsername(SecurityUtil.getCurrentUsername())).thenReturn(Optional.ofNullable(member));

        ResponseResult responseResult = new ResponseResult(HttpStatus.CREATED.value(), "2023-01-30");

        String fileName = "tuxCoding.jpg";
        MockMultipartFile mediaFile = new MockMultipartFile("files", fileName, "image/jpeg", new FileInputStream("/Users/10000doo/Documents/wallpaper/" + fileName));
        List<MultipartFile> mediaFileList = new ArrayList<>();
        mediaFileList.add(mediaFile);
        //when
        ResponseEntity result = diaryService.saveDiary(writeDiaryDto, mediaFileList);

        //then
        assertThat(result.getBody()).isEqualTo(responseResult);
    }

    @Test
    public void getDiaryList() {
        //given
        given(SecurityUtil.getCurrentUsername()).willReturn("username");
        List<Diary> diaries = new ArrayList<>();

        for (int i = 1; i < 21; i++) {
            Member member = Member.builder()
                    .username("username")
                    .email("test@test.com")
                    .loginId("testId3")
                    .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                    .role(MemberType.USER)
                    .password("testPassword3!")
                    .field("서울시")
                    .build();

            ExerciseInfo exInfo = ExerciseInfo.builder()
                    .exerciseName("pushUp")
                    .reps(10)
                    .exSetCount(10)
                    .cardio(false)
                    .cardioTime(0)
                    .finished(true)
                    .build();

            ExerciseInfo exInfo2 = ExerciseInfo.builder()
                    .exerciseName("jogging")
                    .reps(0)
                    .exSetCount(0)
                    .cardio(true)
                    .cardioTime(30)
                    .finished(false)
                    .build();

            List<ExerciseInfo> exInfoList = new ArrayList<>();
            exInfoList.add(exInfo);
            exInfoList.add(exInfo2);

            Diary diary = Diary.builder()
                    .member(member)
                    .exerciseInfo(exInfoList)
                    .review("열심히 했다 오운완")
                    .exerciseDate("2023-01-" + i)
                    .dateTime(new DateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), null))
                    .build();

            diaries.add(diary);
        }

        Mockito.when(diaryRepository.findDiariesByUsername(SecurityUtil.getCurrentUsername(), "2023-01")).thenReturn(diaries);
        //when
        ResponseEntity diaryList = diaryService.getDiaryList("2023-01");
        //then
        assertThat(diaryList.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
