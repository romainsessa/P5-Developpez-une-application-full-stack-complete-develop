package com.openclassrooms.mddapi.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import com.openclassrooms.mddapi.dto.TopicDTO;
import com.openclassrooms.mddapi.entity.Topic;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.NotFoundException;
import com.openclassrooms.mddapi.mapper.TopicMapper;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.impl.TopicService;

@ExtendWith(MockitoExtension.class)
class TopicServiceTest {

    private TopicRepository topicRepository;
    private TopicMapper topicMapper;
    private UserRepository userRepository;

    private TopicService topicService;

    @BeforeEach
    void setUp() {
        topicRepository = mock(TopicRepository.class);
        topicMapper = mock(TopicMapper.class);
        userRepository = mock(UserRepository.class);

        topicService = new TopicService(topicRepository, topicMapper, userRepository);
    }

    @Test
    void should_get_all_topics() {

        List<Topic> topics = List.of(new Topic());

        when(topicRepository.findAll()).thenReturn(topics);
        when(topicMapper.toDto(topics)).thenReturn(List.of(new TopicDTO(null, null, null)));

        List<TopicDTO> result = topicService.getTopics();

        assertEquals(1, result.size());
        verify(topicRepository).findAll();
    }

    @Test
    void should_subscribe_user_to_topic() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        User user = new User();
        user.setId(1L);

        Topic topic = new Topic();
        topic.setUsers(new ArrayList<>());

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));

        topicService.subscribe(1L, auth);

        assertTrue(topic.getUsers().contains(user));
        verify(topicRepository).save(topic);
    }

    @Test
    void should_not_subscribe_if_already_subscribed() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        User user = new User();
        user.setId(1L);

        Topic topic = new Topic();
        topic.setUsers(new ArrayList<>(List.of(user)));

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(topicRepository.findById(1L)).thenReturn(Optional.of(topic));

        topicService.subscribe(1L, auth);

        verify(topicRepository, never()).save(topic);
    }

    @Test
    void should_throw_if_user_not_found_on_subscribe() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> topicService.subscribe(1L, auth));
    }

    @Test
    void should_throw_if_topic_not_found_on_subscribe() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        User user = new User();

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(topicRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> topicService.subscribe(1L, auth));
    }

    @Test
    void should_unsubscribe_user() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        User user = new User();
        user.setId(1L);

        Topic topic = new Topic();
        topic.setUsers(new ArrayList<>(List.of(user)));

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(topicRepository.findById(any())).thenReturn(Optional.of(topic));

        topicService.unsubscribe(1L, auth);

        assertFalse(topic.getUsers().contains(user));
        verify(topicRepository).save(topic);
    }

    @Test
    void should_not_unsubscribe_if_not_subscribed() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        User user = new User();
        user.setId(1L);

        Topic topic = new Topic();
        topic.setUsers(new ArrayList<>());

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(topicRepository.findById(any())).thenReturn(Optional.of(topic));

        topicService.unsubscribe(1L, auth);

        verify(topicRepository, never()).save(topic);
    }

    @Test
    void should_throw_if_user_not_found_on_unsubscribe() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> topicService.unsubscribe(1L, auth));
    }

    @Test
    void should_throw_if_topic_not_found_on_unsubscribe() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        User user = new User();

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(topicRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> topicService.unsubscribe(1L, auth));
    }
}