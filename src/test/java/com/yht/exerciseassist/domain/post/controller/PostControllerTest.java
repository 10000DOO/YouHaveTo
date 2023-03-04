package com.yht.exerciseassist.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yht.exerciseassist.domain.factory.PostFactory;
import com.yht.exerciseassist.domain.post.dto.WritePostDto;
import com.yht.exerciseassist.domain.post.service.PostService;
import com.yht.exerciseassist.exception.CommonExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    @Value("${test.address}")
    private String testAddress;

    @Test
    @WithMockUser
    public void writePost() throws Exception {
        //given
        WritePostDto writePostDto = PostFactory.writePostDto();
        String writePostDtoJson = objectMapper.writeValueAsString(writePostDto);

        String fileName = "tuxCoding.jpg";
        MockMultipartFile mediaFile = new MockMultipartFile("files", fileName, "image/jpeg", new FileInputStream(testAddress + fileName));
        MockMultipartFile jsonFile = new MockMultipartFile("writePostDto", writePostDtoJson, "application/json", writePostDtoJson.getBytes(StandardCharsets.UTF_8));
        //when
        mockMvc.perform(multipart("/post/write")
                        .file(mediaFile)
                        .file(jsonFile)
                        .with(csrf()))
                .andExpect(status().isCreated());
        //then
    }

    @Test
    @WithMockUser
    public void postDetail() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = get("/post/detail/{postId}", 1);
        //when
        mockMvc.perform((builder)
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }

    @Test
    @WithMockUser
    public void dataForEdit() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = get("/post/edit/{postId}", 1);
        //when
        mockMvc.perform((builder)
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }

    @Test
    @WithMockUser
    public void editPost() throws Exception {
        //given
        WritePostDto writePostDto = PostFactory.writePostDto();
        String writePostDtoJson = objectMapper.writeValueAsString(writePostDto);

        String fileName = "tuxCoding.jpg";
        MockMultipartFile mediaFile = new MockMultipartFile("files", fileName, "image/jpeg", new FileInputStream(testAddress + fileName));
        MockMultipartFile jsonFile = new MockMultipartFile("writePostDto", writePostDtoJson, "application/json", writePostDtoJson.getBytes(StandardCharsets.UTF_8));


        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/post/edit/{id}", 1);
        builder.with(request -> {
            request.setMethod("PATCH");
            return request;
        });
        //when
        mockMvc.perform((builder)
                        .file(mediaFile)
                        .file(jsonFile)
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }

    @Test
    @WithMockUser
    public void deletePost() throws Exception {
        //given
        //when
        mockMvc.perform(MockMvcRequestBuilders.patch("/post/delete/{postId}", 1)
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }

    @Test
    @WithMockUser
    public void pressLike() throws Exception {
        //given

        //when
        mockMvc.perform(MockMvcRequestBuilders.patch("/post/like")
                        .param("post_id", "1")
                        .param("clicked", "false")
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }

    @Test
    @WithMockUser
    public void unlike() throws Exception {
        //given

        //when
        mockMvc.perform(MockMvcRequestBuilders.patch("/post/like")
                        .param("post_id", "1")
                        .param("clicked", "true")
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }
}
