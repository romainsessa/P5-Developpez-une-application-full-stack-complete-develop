package com.openclassrooms.mddapi.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.openclassrooms.mddapi.payload.request.LoginRequest;
import com.openclassrooms.mddapi.payload.request.RegisterRequest;

import jakarta.transaction.Transactional;
import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_register_user_and_return_token() throws Exception {

        RegisterRequest request = new RegisterRequest(
                "test",
                "test@mail.com",
                "Password123!"
        );

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }
    
    @Test
    void should_register_user_and_return_exception() throws Exception {

        RegisterRequest request1 = new RegisterRequest(
                "test",
                "test@mail.com",
                "Password123!"
        );

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
        
        RegisterRequest request2 = new RegisterRequest(
                "test1",
                "test@mail.com",
                "Password123!"
        );
        
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isBadRequest());
        
        RegisterRequest request3 = new RegisterRequest(
                "test",
                "test1@mail.com",
                "Password123!"
        );
        
        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request3)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_login_user_and_return_token() throws Exception {

        RegisterRequest register = new RegisterRequest(
                "test1",
                "test1@mail.com",
                "Password123!"
        );

        mockMvc.perform(post("/api/auth/register")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isOk());

        LoginRequest login = new LoginRequest(
                "test1@mail.com",
                "Password123!"
        );

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void should_fail_login_with_wrong_credentials() throws Exception {

        LoginRequest login = new LoginRequest(
                "wrong@mail.com",
                "wrongPassword"
        );

        mockMvc.perform(post("/api/auth/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }
}