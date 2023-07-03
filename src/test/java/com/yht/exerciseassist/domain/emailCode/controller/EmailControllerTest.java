package com.yht.exerciseassist.domain.emailCode.controller;

import com.yht.exerciseassist.domain.emailCode.service.EmailService;
import com.yht.exerciseassist.exception.CommonExceptionHandler;
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

@WebMvcTest(value = EmailController.class)
@ActiveProfiles("test")
class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommonExceptionHandler commonExceptionHandler;
    @MockBean
    private EmailService emailService;

    @Test
    @WithMockUser
    public void checkEmailCode() throws Exception {
        //given

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/email/check")
                        .param("code", "VMQfdgrso150")
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }
}
