package com.yht.exerciseassist.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yht.exerciseassist.domain.factory.PostFactory;
import com.yht.exerciseassist.domain.post.dto.WritePostDto;
import com.yht.exerciseassist.domain.post.service.PostService;
import com.yht.exerciseassist.exceoption.CommonExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = PostController.class)
@ActiveProfiles("test")
class PostControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private PostService postService;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommonExceptionHandler commonExceptionHandler;

    @Test
    @WithMockUser
    public void writePost() throws Exception {
        //given
        WritePostDto writePostDto = PostFactory.writePostDto();
        String writePostDtoJson = objectMapper.writeValueAsString(writePostDto);

        String fileName = "tuxCoding.jpg";
        MockMultipartFile mediaFile = new MockMultipartFile("files", fileName, "image/jpeg", new FileInputStream("/Users/10000doo/Documents/wallpaper/" + fileName));///Users/jeong-yunju/Documents/wallpaper
        MockMultipartFile jsonFile = new MockMultipartFile("writePostDto", writePostDtoJson, "application/json", writePostDtoJson.getBytes(StandardCharsets.UTF_8));
        //when
        mockMvc.perform(multipart("/post/write")
                        .file(mediaFile)
                        .file(jsonFile)
                        .with(csrf()))
                .andExpect(status().isCreated());
        //then
    }
}
