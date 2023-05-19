package com.yht.exerciseassist.admin.accuse.controller;

import com.yht.exerciseassist.admin.accuse.service.AdminAccuseService;
import com.yht.exerciseassist.exception.CommonExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AdminAccuseController.class)
@ActiveProfiles("test")
class AdminAccuseControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommonExceptionHandler commonExceptionHandler;
    @MockBean
    private AdminAccuseService adminAccuseService;

    @Test
    @WithMockUser
    public void getAccuseList() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = get("/admin/accuse")
                .param("accuseType", "ABUSIVE_LANGUAGE_BELITTLE")
                .param("accuseGetType", "POST")
                .param("page", "0")
                .param("size", "2");
        //when
        mockMvc.perform((builder)
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }

    @Test
    @WithMockUser
    public void deleteAccuse() throws Exception {
        //given

        //when
        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/accuse/delete/{accuseId}", 1)
                        .with(csrf()))
                .andExpect(status().isOk());

        //then
    }
}