package com.yht.exerciseassist.domain.diary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yht.exerciseassist.domain.diary.dto.ExerciseInfoDto;
import com.yht.exerciseassist.domain.diary.dto.WriteDiaryDto;
import com.yht.exerciseassist.domain.diary.service.DiaryService;
import com.yht.exerciseassist.exceoption.CommonExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = DiaryController.class)
class DiaryControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    ResourceLoader loader;
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
        exerciseInfoDto.setFinished(true);

        List<ExerciseInfoDto> exerciseInfoDtoList = new ArrayList<>();
        exerciseInfoDtoList.add(exerciseInfoDto);

        WriteDiaryDto writeDiaryDto = new WriteDiaryDto();
        writeDiaryDto.setExerciseInfo(exerciseInfoDtoList);
        writeDiaryDto.setReview("오늘 운동 끝");
        writeDiaryDto.setExerciseDate("2023-01-30");

        String writeDiaryDtoJson = objectMapper.writeValueAsString(writeDiaryDto);

        String fileName = "2.png";
        MockMultipartFile mediaFile = new MockMultipartFile("files", fileName, "image/png", new FileInputStream("/Users/10000doo/Desktop/" + fileName));
        MockMultipartFile jsonFile = new MockMultipartFile("writeDiaryDto", writeDiaryDtoJson, "application/json", writeDiaryDtoJson.getBytes(StandardCharsets.UTF_8));

        //when
        mockMvc.perform(multipart("/diary/write")
                        .file(mediaFile)
                        .file(jsonFile)
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }
}
