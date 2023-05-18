package com.yht.exerciseassist.admin.comment.controller;

import com.yht.exerciseassist.domain.comment.service.CommentService;
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

@WebMvcTest(value = AdminCommentController.class)
@ActiveProfiles("test")
class AdminCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private CommonExceptionHandler commonExceptionHandler;

    @Test
    @WithMockUser
    void deleteAdminComment() throws Exception{
        //given

        //when
        mockMvc.perform(MockMvcRequestBuilders.patch("/admin/comment/delete/{commentId}", 1)
                        .with(csrf()))
                .andExpect(status().isOk());

        //then
    }

    @Test
    @WithMockUser
    void getAdminComment() throws Exception{
        //given

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/comment")
                        .param("postId", "1")
                        .param("parentId", "1")
                        .param("page", "0")
                        .param("size", "2")
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }
}