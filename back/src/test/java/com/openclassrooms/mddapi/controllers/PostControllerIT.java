package com.openclassrooms.mddapi.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.payload.request.CreateCommentRequest;
import com.openclassrooms.mddapi.payload.request.CreatePostRequest;
import com.openclassrooms.mddapi.repository.PostRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;

import jakarta.transaction.Transactional;
import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
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
class PostControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;
    
    private static Post post;
    

    private User createUser() {
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@mail.com");
        user.setPassword(passwordEncoder.encode("Password123!"));
        return userRepository.save(user);
    }

    private Topic createTopic() {
        Topic topic = new Topic();
        topic.setName("Angular");
        topic.setDescription("Frontend");
        return topicRepository.save(topic);
    }

    private Post createPost(User user, Topic topic) {
        Post post = new Post();
        post.setTitle("Test post");
        post.setContent("Content");
        post.setAuthor(user);
        post.setTopic(topic);
        return postRepository.save(post);
    }
    
    @BeforeEach
    public void init() {
    	User user = createUser();
        Topic topic = createTopic();
        post = createPost(user, topic);
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    void should_get_posts() throws Exception {        

        mockMvc.perform(get("/api/post"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    void should_get_post_by_id() throws Exception {

        mockMvc.perform(get("/api/post/" + post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test post"));
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    void should_create_post() throws Exception {

        Topic topic = createTopic();

        CreatePostRequest request = new CreatePostRequest(
                topic.getId(),
                "New post",
                "Content"
        );

        mockMvc.perform(post("/api/post")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNumber());
    }

    @Test
    @WithMockUser(username = "test@mail.com")
    void should_add_comment() throws Exception {

        CreateCommentRequest request = new CreateCommentRequest(
                "Nice post"
        );

        mockMvc.perform(post("/api/post/" + post.getId() + "/comments")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}