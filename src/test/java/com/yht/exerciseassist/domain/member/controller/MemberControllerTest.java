package com.yht.exerciseassist.domain.member.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.yht.exerciseassist.domain.factory.MemberFactory;
import com.yht.exerciseassist.domain.member.dto.*;
import com.yht.exerciseassist.domain.member.service.MemberService;
import com.yht.exerciseassist.exception.CommonExceptionHandler;
import com.yht.exerciseassist.jwt.dto.TokenInfo;
import com.yht.exerciseassist.util.ResponseResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = MemberController.class)
@ActiveProfiles("test")
class MemberControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();
    @MockBean
    private MemberService memberService;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommonExceptionHandler commonExceptionHandler;

    @Test
    @WithMockUser()
    public void signUp() throws Exception {
        //given
        SignUpRequestDto signUpRequestDto = MemberFactory.createTestSignUpRequestDto();
        String code = "123456789ABC";
        ResponseResult result = new ResponseResult(HttpStatus.CREATED.value(), signUpRequestDto.getUsername());

        given(memberService.join(signUpRequestDto, code)).willReturn(result);
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .content(objectMapper.writeValueAsString(signUpRequestDto))
                        .param("code", code)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isCreated());
        //then
        assertThat(result.getStatus()).isEqualTo(201);
        assertThat(result.getData()).isEqualTo(signUpRequestDto.getUsername());
    }

    @Test
    @WithMockUser()
    void signIn() throws Exception {
        //given
        SignInRequestDto signInRequestDto = MemberFactory.createTestSignInRequestDto();

        TokenInfo tokenInfo = MemberFactory.createTestTokenInfo();

        ResponseResult result = new ResponseResult(HttpStatus.OK.value(), tokenInfo);
        given(memberService.signIn(signInRequestDto.getLoginId(), signInRequestDto.getPassword())).willReturn(result);
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

    @Test
    @WithMockUser()
    public void deleteMember() throws Exception {
        //given
        PWDto pwDto = new PWDto("testPassword1!");
        //when
        mockMvc.perform(MockMvcRequestBuilders.patch("/member/delete")
                        .content(objectMapper.writeValueAsString(pwDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());
        //then

    }

    @Test
    @WithMockUser()
    public void memberPage() throws Exception {
        //given

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/member/info")
                        .param("username", "10000DOO")
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }

    @Test
    @WithMockUser
    public void findId() throws Exception {
        //given

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/find/id")
                        .param("code", "2eflijlsef")
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }

    @Test
    @WithMockUser()
    public void findPW() throws Exception {
        //given
        FindPWDto findPWDto = new FindPWDto("testPassword3!", "test@test.com");
        //when
        mockMvc.perform(MockMvcRequestBuilders.patch("/find/pw")
                        .content(objectMapper.writeValueAsString(findPWDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }

    @Test
    @WithMockUser()
    public void editMember() throws Exception {
        //given
        EditMemberDto editMemberDto = new EditMemberDto("MANDOO", "testPassword1!", "남양주시", 1L);
        String editMemberDtoJson = objectMapper.writeValueAsString(editMemberDto);
        MockMultipartFile jsonFile = new MockMultipartFile("editMemberDto", editMemberDtoJson, "application/json", editMemberDtoJson.getBytes(StandardCharsets.UTF_8));
        //when
        mockMvc.perform(multipart("/member/edit")
                        .file(jsonFile)
                        .with(csrf())
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andExpect(status().isOk());

        //then
    }

    @Test
    @WithMockUser
    public void passwordCheck() throws Exception {
        //given
        PasswordCheckDto passwordCheckDto = new PasswordCheckDto();
        passwordCheckDto.setPassword("testPassword2!");
        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/member/password/check")
                        .content(objectMapper.writeValueAsString(passwordCheckDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk());
        //then
    }
}
