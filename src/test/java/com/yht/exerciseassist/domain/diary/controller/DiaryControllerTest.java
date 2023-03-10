package com.yht.exerciseassist.domain.diary.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yht.exerciseassist.domain.diary.dto.WriteDiaryDto;
import com.yht.exerciseassist.domain.diary.service.DiaryService;
import com.yht.exerciseassist.domain.factory.DiaryFactory;
import com.yht.exerciseassist.exception.CommonExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = DiaryController.class)
@ActiveProfiles("test")
class DiaryControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private DiaryService diaryService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommonExceptionHandler commonExceptionHandler;

    @Value("${test.address}")
    private String testAddress;

    @Test
    @WithMockUser
    public void writeDiary() throws Exception {
        //given
        WriteDiaryDto writeDiaryDto = DiaryFactory.createTestWriteDiaryDto();

        String writeDiaryDtoJson = objectMapper.writeValueAsString(writeDiaryDto);

        String fileName = "tuxCoding.jpg";
        MockMultipartFile mediaFile = new MockMultipartFile("files", fileName, "image/jpeg", new FileInputStream(testAddress + fileName));
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
    public void dataForEdit() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = get("/diary/edit/{diaryId}", 1);
        //when
        mockMvc.perform((builder)
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }

    @Test
    @WithMockUser
    public void diaryEdit() throws Exception {
        //given
        WriteDiaryDto writeDiaryDto = DiaryFactory.createTestWriteDiaryDto();

        String writeDiaryDtoJson = objectMapper.writeValueAsString(writeDiaryDto);

        String fileName = "tuxCoding.jpg";
        MockMultipartFile mediaFile = new MockMultipartFile("files", fileName, "image/jpeg", new FileInputStream(testAddress + fileName));
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

    @Test
    @WithMockUser
    public void deleteDiary() throws Exception {

        //when
        mockMvc.perform(MockMvcRequestBuilders.patch("/diary/delete/{diaryId}", 1)
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }
}
