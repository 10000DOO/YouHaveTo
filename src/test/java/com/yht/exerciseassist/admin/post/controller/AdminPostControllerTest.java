package com.yht.exerciseassist.admin.post.controller;

import com.yht.exerciseassist.domain.post.service.PostService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AdminPostController.class)
@ActiveProfiles("test")
class AdminPostControllerTest {
    @MockBean
    private PostService postService;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommonExceptionHandler commonExceptionHandler;

    @Test
    @WithMockUser
    public void getAdminPostList() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = get("/admin/post")
                .param("postType", "FREE")
                .param("workOutCategory", "YOGA,ETC")
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
    void adminPostDetail() throws Exception {
        //given
        MockHttpServletRequestBuilder builder = get("/admin/post/detail/{postId}", 1);
        //when
        mockMvc.perform((builder)
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }

    @Test
    @WithMockUser
    public void adminDeletePost() throws Exception {
        //given
        //when
        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/post/delete/{postId}", 1)
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }
}