package com.yht.exerciseassist.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yht.exerciseassist.domain.comment.dto.WriteCommentDto;
import com.yht.exerciseassist.domain.comment.service.CommentService;
import com.yht.exerciseassist.domain.factory.CommentFactory;
import com.yht.exerciseassist.exception.CommonExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CommentController.class)
@ActiveProfiles("test")
class CommentControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private CommonExceptionHandler commonExceptionHandler;

    @Test
    @WithMockUser
    public void writeComment() throws Exception {
        //given
        WriteCommentDto writeCommentDto = CommentFactory.writeCommentDto();
        String writeCommentDtoJson = objectMapper.writeValueAsString(writeCommentDto);

        MockHttpServletRequestBuilder builder = post("/comment/write");

        //when
        mockMvc.perform((builder)
                        .content(writeCommentDtoJson)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(csrf()))
                .andExpect(status().isCreated());

        //then
    }

}