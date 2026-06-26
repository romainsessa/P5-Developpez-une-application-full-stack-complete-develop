package com.openclassrooms.mddapi.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import com.openclassrooms.mddapi.dto.PostDTO;
import com.openclassrooms.mddapi.entity.Comment;
import com.openclassrooms.mddapi.entity.Post;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.NotFoundException;
import com.openclassrooms.mddapi.mapper.PostMapper;
import com.openclassrooms.mddapi.payload.request.CreateCommentRequest;
import com.openclassrooms.mddapi.payload.request.CreatePostRequest;
import com.openclassrooms.mddapi.repository.*;
import com.openclassrooms.mddapi.service.impl.PostService;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private TopicRepository topicRepository;
    private UserRepository userRepository;
    private PostMapper postMapper;

    private PostService postService;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        postRepository = mock(PostRepository.class);
        topicRepository = mock(TopicRepository.class);
        userRepository = mock(UserRepository.class);
        postMapper = mock(PostMapper.class);

        postService = new PostService(
                commentRepository,
                postRepository,
                topicRepository,
                userRepository,
                postMapper
        );
    }

    @Test
    void should_create_post() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        User user = new User();
        Topic topic = new Topic();

        CreatePostRequest request = new CreatePostRequest(1L, "title", "content");

        Post saved = new Post();
        saved.setId(1L);

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));
        when(postRepository.save(any())).thenReturn(saved);

        Long result = postService.createPost(request, auth);

        assertEquals(1L, result);
        verify(postRepository).save(any());
    }

    @Test
    void should_throw_if_user_not_found_on_create() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        CreatePostRequest request = new CreatePostRequest(1L, "title", "content");

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> postService.createPost(request, auth));
    }

    @Test
    void should_throw_if_topic_not_found_on_create() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        User user = new User();
        CreatePostRequest request = new CreatePostRequest(1L, "title", "content");

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(topicRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> postService.createPost(request, auth));
    }

    @Test
    void should_get_all_posts() {

        List<Post> posts = List.of(new Post());

        when(postRepository.findAll()).thenReturn(posts);
        when(postMapper.toDto(posts)).thenReturn(List.of(new PostDTO(null, null, null, null, null, null, null)));

        List<PostDTO> result = postService.getAllPosts();

        assertEquals(1, result.size());
    }

    @Test
    void should_get_post_by_id() {

        Post post = new Post();

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postMapper.toDto(post)).thenReturn(new PostDTO(null, null, null, null, null, null, null));

        PostDTO result = postService.getPostById(1L);

        assertNotNull(result);
    }

    @Test
    void should_throw_if_post_not_found() {

        when(postRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> postService.getPostById(1L));
    }

    @Test
    void should_get_posts_from_user_topics() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        Topic topic = new Topic();
        User user = new User();
        user.setTopics(List.of(topic));

        List<Post> posts = List.of(new Post());

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(postRepository.findByTopicIn(any())).thenReturn(posts);
        when(postMapper.toDto(posts)).thenReturn(List.of(new PostDTO(null, null, null, null, null, null, null)));

        List<PostDTO> result = postService.getPostsFromTopic(auth);

        assertEquals(1, result.size());
    }

    @Test
    void should_return_empty_if_no_subscriptions() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        User user = new User();
        user.setTopics(new ArrayList<>());

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        List<PostDTO> result = postService.getPostsFromTopic(auth);

        assertTrue(result.isEmpty());
    }

    @Test
    void should_add_comment() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        User user = new User();
        Post post = new Post();

        CreateCommentRequest request = new CreateCommentRequest("content");

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(postRepository.findById(any())).thenReturn(Optional.of(post));

        postService.addComment(1L, request, auth);

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void should_throw_if_user_not_found_add_comment() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> postService.addComment(1L, new CreateCommentRequest("c"), auth));
    }

    @Test
    void should_throw_if_post_not_found_add_comment() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        User user = new User();

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(postRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> postService.addComment(1L, new CreateCommentRequest("c"), auth));
    }
}