package com.yht.exerciseassist.domain.diary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yht.exerciseassist.domain.diary.BodyPart;
import com.yht.exerciseassist.domain.diary.dto.ExerciseInfoDto;
import com.yht.exerciseassist.domain.diary.dto.WriteDiaryDto;
import com.yht.exerciseassist.domain.diary.service.DiaryService;
import com.yht.exerciseassist.exceoption.CommonExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = DiaryController.class)
@ActiveProfiles("test")
class DiaryControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();
    //    @Autowired
//    ResourceLoader loader;
    @MockBean
    private DiaryService diaryService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommonExceptionHandler commonExceptionHandler;

    @Test
    @WithMockUser
    public void writeDiary() throws Exception {
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

        String writeDiaryDtoJson = objectMapper.writeValueAsString(writeDiaryDto);

        String fileName = "tuxCoding.jpg";
        MockMultipartFile mediaFile = new MockMultipartFile("files", fileName, "image/jpeg", new FileInputStream("/Users/10000doo/Documents/wallpaper/" + fileName));///Users/jeong-yunju/Documents/wallpaper
        MockMultipartFile jsonFile = new MockMultipartFile("writeDiaryDto", writeDiaryDtoJson, "application/json", writeDiaryDtoJson.getBytes(StandardCharsets.UTF_8));

        //when
        mockMvc.perform(multipart("/diary/write")
                        .file(mediaFile)
                        .file(jsonFile)
                        .with(csrf()))
                .andExpect(status().isCreated());
        //then
    }

    @Test
    @WithMockUser
    public void getDiaryList() throws Exception {
        //given

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/diary")
                        .param("date", "2023-02")
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }

    @Test
    @WithMockUser
    public void getDiaryDetail() throws Exception {

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/diary/detail")
                        .param("date", "2023-02-23")
                        .with(csrf()))
                .andExpect(status().isOk());
        //then

    }

    @Test
    @WithMockUser
    public void diaryEdit() throws Exception {
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

        String writeDiaryDtoJson = objectMapper.writeValueAsString(writeDiaryDto);

        String fileName = "tuxCoding.jpg";
        MockMultipartFile mediaFile = new MockMultipartFile("files", fileName, "image/jpeg", new FileInputStream("/Users/10000doo/Documents/wallpaper/" + fileName));///Users/jeong-yunju/Documents/wallpaper
        MockMultipartFile jsonFile = new MockMultipartFile("writeDiaryDto", writeDiaryDtoJson, "application/json", writeDiaryDtoJson.getBytes(StandardCharsets.UTF_8));

        //when
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/diary/edit/{id}", 1);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PATCH");
                return request;
            }
        });

        mockMvc.perform((builder)
                        .file(mediaFile)
                        .file(jsonFile)
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }
}
