package com.yht.exerciseassist.domain.member.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.yht.exerciseassist.ResponseResult;
import com.yht.exerciseassist.domain.member.dto.SignInRequestDto;
import com.yht.exerciseassist.domain.member.dto.SignUpRequestDto;
import com.yht.exerciseassist.domain.member.service.MemberService;
import com.yht.exerciseassist.jwt.dto.TokenInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = MemberController.class)
class MemberControllerTest {

    @MockBean
    private MemberService memberService;

    @Autowired
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();



    @Test
    @WithMockUser()
    public void signUp() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = new SignUpRequestDto();
        signUpRequestDto.setUsername("username");
        signUpRequestDto.setPassword("testPassword3!");
        signUpRequestDto.setEmail("test@test.com");
        signUpRequestDto.setLoginId("testId3");
        signUpRequestDto.setField("서울시");

        ResponseResult result = new ResponseResult(HttpStatus.CREATED.value(), signUpRequestDto.getUsername());

        given(memberService.join(any()))
                .willReturn(result);
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .content(objectMapper.writeValueAsString(signUpRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
        assertThat(result.getStatus()).isEqualTo(201);
        assertThat(result.getData()).isEqualTo(signUpRequestDto.getUsername());
    }

    @Test
    @WithMockUser()
    void signIn() throws Exception {
        //given
        SignInRequestDto signInRequestDto = new SignInRequestDto("loginId1", "password1!");
        TokenInfo tokenInfo = new TokenInfo("Bearer", "access", "refresh");
        ResponseResult result = new ResponseResult(HttpStatus.OK.value(), tokenInfo);
        given(memberService.signIn(any(),any())).willReturn(result);
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/signin")
                .content(objectMapper.writeValueAsString(signInRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk());
        //then
        assertThat(result.getStatus()).isEqualTo(200);
        assertThat(result.getData()).isEqualTo(tokenInfo);
    }
}