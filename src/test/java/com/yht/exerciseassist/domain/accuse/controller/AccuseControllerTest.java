package com.yht.exerciseassist.domain.accuse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yht.exerciseassist.domain.accuse.dto.AccuseReq;
import com.yht.exerciseassist.domain.accuse.service.AccuseService;
import com.yht.exerciseassist.domain.factory.AccuseFactory;
import com.yht.exerciseassist.exception.CommonExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AccuseController.class)
@ActiveProfiles("test")
class AccuseControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommonExceptionHandler commonExceptionHandler;
    @MockBean
    private AccuseService accuseService;

    @Test
    @WithMockUser
    public void savePostAccuse() throws Exception {
        //given
        AccuseReq accuseReq = AccuseFactory.createAccuseReq();
        String accuserReqJson = objectMapper.writeValueAsString(accuseReq);
        //when
        mockMvc.perform(post("/accuse/post/save/{postId}", 1)
                        .contentType("application/json")
                        .content(accuserReqJson)
                        .with(csrf()))
                .andExpect(status().isCreated());
        //then
    }

    @Test
    @WithMockUser
    public void saveCommentAccuse() throws Exception {
        //given
        AccuseReq accuseReq = AccuseFactory.createAccuseReq();
        String accuserReqJson = objectMapper.writeValueAsString(accuseReq);
        //when
        mockMvc.perform(post("/accuse/comment/save/{commentId}", 1)
                        .contentType("application/json")
                        .content(accuserReqJson)
                        .with(csrf()))
                .andExpect(status().isCreated());
        //then
    }
}
