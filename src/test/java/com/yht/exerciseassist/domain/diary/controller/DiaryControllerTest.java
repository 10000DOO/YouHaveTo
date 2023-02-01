package com.yht.exerciseassist.domain.diary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.diary.dto.WriteDiaryDto;
import com.yht.exerciseassist.domain.diary.service.DiaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = DiaryController.class)
class DiaryControllerTest {

    @MockBean
    private DiaryService diaryService;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser
    public void writeDiary() throws Exception {
        //given
        WriteDiaryDto writeDiaryDto = new WriteDiaryDto();
        writeDiaryDto.setExerciseName("pushUp");
        writeDiaryDto.setReps(10);
        writeDiaryDto.setCardio(true);
        writeDiaryDto.setExSetCount(10);
        writeDiaryDto.setReview("오늘 운동 끝");
        writeDiaryDto.setCardioTime(30);
        writeDiaryDto.setExerciseDate("2023-01-30");

        ResponseResult result = new ResponseResult(HttpStatus.CREATED.value(), 1);
        given(diaryService.saveDiary(writeDiaryDto)).willReturn(
                result
        );
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/diary/write")
                        .content(objectMapper.writeValueAsString(writeDiaryDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
        assertThat(result.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(result.getData()).isEqualTo(1);
    }
}