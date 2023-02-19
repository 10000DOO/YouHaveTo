package com.yht.exerciseassist.domain.media.controller;

import com.yht.exerciseassist.domain.media.service.MediaService;
import com.yht.exerciseassist.exceoption.CommonExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = MediaController.class)
@ActiveProfiles("test")
class MediaControllerTest {
    @MockBean
    private MediaService mediaService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommonExceptionHandler commonExceptionHandler;

    @Test
    @WithMockUser
    public void getMedia() throws Exception {
        Long id = 1L;
        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/media/" + id)
                        .with(csrf()))
                .andExpect(status().isOk());


    }


}