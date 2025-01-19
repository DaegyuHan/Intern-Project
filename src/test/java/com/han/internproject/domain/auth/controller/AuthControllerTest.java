package com.han.internproject.domain.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.han.internproject.domain.auth.dto.request.SigninRequestDto;
import com.han.internproject.domain.auth.dto.request.SignupRequestDto;
import com.han.internproject.domain.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private SignupRequestDto signupRequestDto;
    private SigninRequestDto signinRequestDto;

    @BeforeEach
    void setUp() {
        signupRequestDto = new SignupRequestDto("username", "password", "nickname");
        signinRequestDto = new SigninRequestDto("username", "password");
    }

    @Test
    public void 회원가입_성공_Controller() throws Exception {
        String stringDto = objectMapper.writeValueAsString(signupRequestDto);
        mockMvc.perform(post("/signup")
                        .contentType("application/json")
                        .content(stringDto)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void 로그인_성공_Controller() throws Exception {
        authService.signup(signupRequestDto);
        String stringDto = objectMapper.writeValueAsString(signinRequestDto);
        mockMvc.perform(post("/sign")
                        .contentType("application/json")
                        .content(stringDto)
                )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}