package com.openclassrooms.mddapi.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.payload.request.UpdateUserRequest;
import com.openclassrooms.mddapi.repository.UserRepository;

import jakarta.transaction.Transactional;
import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User createUser() {
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@mail.com");
        user.setPassword(passwordEncoder.encode("Password123!"));
        return userRepository.save(user);
    }
    
    @Test
    @WithMockUser(username = "test@mail.com")
    void should_get_current_user() throws Exception {

        createUser();

        mockMvc.perform(get("/api/user/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.username").value("test"));
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    void should_update_user() throws Exception {

        createUser();

        UpdateUserRequest request = new UpdateUserRequest(
                "updated",
                "updated@mail.com",
                "Password123!"
        );

        mockMvc.perform(put("/api/user/me")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@mail.com"))
                .andExpect(jsonPath("$.username").value("updated"));

        User updated = userRepository.findByEmail("updated@mail.com").orElseThrow();

        assert(updated.getUsername().equals("updated"));
    }
    
    @Test
    @WithMockUser(username = "test@mail.com")
    void should_update_user_already_used() throws Exception {

        createUser();

        UpdateUserRequest request1 = new UpdateUserRequest(
                "test",
                "updated@mail.com",
                "Password123!"
        );

        mockMvc.perform(put("/api/user/me")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isBadRequest());
        
        UpdateUserRequest request2 = new UpdateUserRequest(
                "update",
                "test@mail.com",
                "Password123!"
        );

        mockMvc.perform(put("/api/user/me")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isBadRequest());
        
        UpdateUserRequest request3 = new UpdateUserRequest(
                "update",
                "update@mail.com",
                "abc"
        );

        mockMvc.perform(put("/api/user/me")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request2)))
                .andExpect(status().isBadRequest());
    }
}
