package com.openclassrooms.mddapi.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;

import jakarta.transaction.Transactional;

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
class TopicControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User createUser() {
    	return createUser("test", "test@mail.com", "Password123!");
    }
    
    private User createUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    private Topic createTopic() {
        Topic topic = new Topic();
        topic.setName("Angular");
        topic.setDescription("Frontend framework");
        return topicRepository.save(topic);
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    void should_get_all_topics() throws Exception {

        createTopic();

        mockMvc.perform(get("/api/topic"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Angular"));
    }

    @Test
    @WithMockUser(username = "test2@mail.com")
    void should_subscribe_to_topic() throws Exception {

        createUser("test2", "test2@mail.com", "Password123!");
        Topic topic = createTopic();

        mockMvc.perform(post("/api/topic/" + topic.getId() + "/subscribe"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    void should_unsubscribe_from_topic() throws Exception {

        createUser();
        Topic topic = createTopic();

        mockMvc.perform(post("/api/topic/" + topic.getId() + "/subscribe"));

        mockMvc.perform(delete("/api/topic/" + topic.getId() + "/unsubscribe"))
                .andExpect(status().isOk());
    }
}
