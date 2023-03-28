package com.yht.exerciseassist.domain.diary.service;

import com.yht.exerciseassist.domain.diary.Diary;
import com.yht.exerciseassist.domain.diary.dto.*;
import com.yht.exerciseassist.domain.diary.repository.DiaryRepository;
import com.yht.exerciseassist.domain.factory.DiaryFactory;
import com.yht.exerciseassist.domain.factory.MediaFactory;
import com.yht.exerciseassist.domain.factory.MemberFactory;
import com.yht.exerciseassist.domain.media.Media;
import com.yht.exerciseassist.domain.media.service.MediaService;
import com.yht.exerciseassist.domain.member.Member;
import com.yht.exerciseassist.domain.member.repository.MemberRepository;
import com.yht.exerciseassist.util.ResponseResult;
import com.yht.exerciseassist.util.SecurityUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
class DiaryServiceTest {

    private static MockedStatic<SecurityUtil> securityUtilMockedStatic;
    DiaryService diaryService;
    @Value("${file.dir}")
    private String fileDir;
    @MockBean
    private DiaryRepository diaryRepository;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private MediaService mediaService;
    @Value("${test.address}")
    private String testAddress;

    @AfterEach
    public void afterAll() {
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
        WriteDiaryDto writeDiaryDto = DiaryFactory.createTestWriteDiaryDto();

        Member member = MemberFactory.createTestMember();

        given(SecurityUtil.getCurrentUsername()).willReturn("username");
        Mockito.when(memberRepository.findByNotDeletedUsername(SecurityUtil.getCurrentUsername())).thenReturn(Optional.ofNullable(member));

        ResponseResult responseResult = new ResponseResult(HttpStatus.CREATED.value(), "2023-01-30");

        String fileName = "tuxCoding.jpg";
        MockMultipartFile mediaFile = new MockMultipartFile("files", fileName, "image/jpeg", new FileInputStream(testAddress + fileName));
        List<MultipartFile> mediaFileList = new ArrayList<>();
        mediaFileList.add(mediaFile);
        //when
        ResponseResult responseResult1 = diaryService.saveDiary(writeDiaryDto, mediaFileList);

        //then
        assertThat(responseResult1).isEqualTo(responseResult);
    }

    @Test
    public void getDiaryList() {
        //given
        given(SecurityUtil.getCurrentUsername()).willReturn("username");
        List<Diary> diaries = new ArrayList<>();

        Member member = MemberFactory.createTestMember();

        for (int i = 1; i < 21; i++) {
            Diary diary = DiaryFactory.createTestDiary(member);
            diaries.add(diary);
        }

        List<Calender> calenderList = new ArrayList<>();

        for (int i = 1; i < 21; i++) {
            Calender calender = new Calender("2023-01-30", 100);
            calenderList.add(calender);
        }

        DiaryListDto diaryListDto = new DiaryListDto(calenderList, 100);

        Mockito.when(diaryRepository.findDiariesByUsername(SecurityUtil.getCurrentUsername(), "2023-01")).thenReturn(diaries);
        //when
        ResponseResult diaryList = diaryService.getDiaryList("2023-01");
        //then
        assertThat(diaryList.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(diaryList.getData()).isEqualTo(diaryListDto);
    }

    @Test
    public void getDiaryDetail() {
        //given
        given(SecurityUtil.getCurrentUsername()).willReturn("username");

        Member member = MemberFactory.createTestMember();

        Media media = MediaFactory.createTeatMedia(fileDir + "test1.png");
        media.setMediaIdUsedOnlyTest(1L);

        List<Media> mediaId = new ArrayList<>();
        mediaId.add(media);

        Diary diaryDetail = DiaryFactory.createTestDiary(member);

        diaryDetail.setDiaryIdUsedOnlyTest(1L);

        diaryDetail.linkToMedia(mediaId);

        Optional<Diary> opDiaryDetail = Optional.of(diaryDetail);

        DiaryDetailDto diaryDetailDto = DiaryFactory.createTestDiaryDetailDto();

        Mockito.when(diaryRepository.findDiaryDetailsByUsername(SecurityUtil.getCurrentUsername(), "2023-01-30")).thenReturn(opDiaryDetail);
        //when
        ResponseResult diary = diaryService.getDiaryDetail("2023-01-30");
        //then
        assertThat(diary.getStatus()).isEqualTo(HttpStatus.OK.value());
        //diary.getData()의 기대값과 똑같은 객체를 하나 만들어!! 그래서 비교해
        assertThat(diary.getData()).isEqualTo(diaryDetailDto);

    }

    @Test
    public void getPostEditData() {
        //given
        Long diaryId = 1L;
        Member testMember = MemberFactory.createTestMember();
        Diary testDiary = DiaryFactory.createTestDiary(testMember);
        Mockito.when(diaryRepository.findByNotDeleteId(diaryId)).thenReturn(Optional.ofNullable(testDiary));

        List<ExerciseInfoResDto> exerciseInfoDto = DiaryFactory.getExerciseInfoDto();
        DiaryEditData diaryEditData = DiaryEditData.builder()
                .review(testDiary.getReview())
                .exerciseInfo(exerciseInfoDto)
                .mediaList(new ArrayList<>())
                .build();

        ResponseResult<DiaryEditData> diaryEditDataResponseResult = new ResponseResult<>(200, diaryEditData);
        //when
        ResponseResult<DiaryEditData> diaryEditDataResult = diaryService.getDiaryEditData(diaryId);
        //then
        assertThat(diaryEditDataResult).isEqualTo(diaryEditDataResponseResult);
    }

    @Test
    public void editDiary() throws IOException {
        //given
        Long id = 1L;

        Member member = MemberFactory.createTestMember();

        Diary diaryDetail = DiaryFactory.createTestDiary(member);

        diaryDetail.setDiaryIdUsedOnlyTest(1L);

        Media media = MediaFactory.createTeatMedia(fileDir + "tuxCoding.jpg");

        List<Media> mediaId = new ArrayList<>();
        mediaId.add(media);

        diaryDetail.linkToMedia(mediaId);

        MockMultipartFile mediaFile = new MockMultipartFile("files", media.getOriginalFilename(), "image/jpeg", new FileInputStream(testAddress + "tuxCoding.jpg"));
        List<MultipartFile> fileList = new ArrayList<>();
        fileList.add(mediaFile);
        Mockito.when(diaryRepository.findByNotDeleteId(id)).thenReturn(Optional.of(diaryDetail));
        Mockito.when(mediaService.uploadMediaToFileSystem(fileList)).thenReturn(mediaId);
        Mockito.when(diaryRepository.save(diaryDetail)).thenReturn(diaryDetail);

        WriteDiaryDto writeDiaryDto = DiaryFactory.createTestWriteDiaryDto();

        ResponseResult<String> result = new ResponseResult<>(200, writeDiaryDto.getExerciseDate());
        //when
        ResponseResult<String> writeDiaryDtoResponseResult = diaryService.editDiary(writeDiaryDto, fileList, id);
        //then
        assertThat(writeDiaryDtoResponseResult).isEqualTo(result);
    }

    @Test
    public void deleteDiary() throws IOException {
        //given
        Long diaryId = 1L;

        given(SecurityUtil.getCurrentUsername()).willReturn("member1");

        Member member = MemberFactory.createTestMember();

        Diary diaryDetail = DiaryFactory.createTestDiary(member);
        diaryDetail.setDiaryIdUsedOnlyTest(diaryId);

        Media media = MediaFactory.createTeatMedia(fileDir + "tuxCoding.jpg");
        media.setMediaIdUsedOnlyTest(1L);
        List<Media> mediaId = new ArrayList<>();
        mediaId.add(media);

        diaryDetail.linkToMedia(mediaId);

        Mockito.when(diaryRepository.findByNotDeleteId(diaryId)).thenReturn(Optional.of(diaryDetail));
        ResponseResult<Long> result = new ResponseResult<>(200, diaryId);
        //when
        ResponseResult<Long> longResponseResult = diaryService.deleteDiary(diaryId);
        //then
        assertThat(longResponseResult).isEqualTo(result);
    }
}
