package com.openclassrooms.mddapi.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.BadRequestException;
import com.openclassrooms.mddapi.exception.NotFoundException;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.payload.request.RegisterRequest;
import com.openclassrooms.mddapi.payload.request.UpdateUserRequest;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.impl.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userMapper = mock(UserMapper.class);
        passwordEncoder = mock(PasswordEncoder.class);

        userService = new UserService(userRepository, userMapper, passwordEncoder);
    }

    @Test
    void should_create_user_successfully() {

        RegisterRequest request = new RegisterRequest("test", "test@mail.com", "Password123!@#A");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");

        User savedUser = new User();
        savedUser.setEmail("test@mail.com");
        savedUser.setUsername("test");
        
        when(userRepository.save(any())).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(new UserDTO(null, "test", "test@mail.com", null, null, null));

        UserDTO result = userService.createUser(request);

        assertEquals("test@mail.com", result.email());
        verify(userRepository).save(any());
    }

    @Test
    void should_throw_if_email_exists() {

        RegisterRequest request = new RegisterRequest("test", "test@mail.com", "Password123!@#A");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.createUser(request));
    }

    @Test
    void should_throw_if_username_exists() {

        RegisterRequest request = new RegisterRequest("test", "test@mail.com", "Password123!@#A");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.createUser(request));
    }

    @Test
    void should_throw_if_password_invalid() {

        RegisterRequest request = new RegisterRequest("test", "test@mail.com", "short");

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByUsername(any())).thenReturn(false);

        assertThrows(BadRequestException.class, () -> userService.createUser(request));
    }

    @Test
    void should_get_user() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        User user = new User();
        user.setEmail("test@mail.com");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(new UserDTO(null, "test", "test@mail.com", null, null, null));

        UserDTO result = userService.getUser(auth);

        assertEquals("test@mail.com", result.email());
    }

    @Test
    void should_throw_if_user_not_found() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUser(auth));
    }

    @Test
    void should_update_user() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        User user = new User();
        user.setEmail("test@mail.com");

        UpdateUserRequest request = new UpdateUserRequest("new", "new@mail.com", "Password123!@#A");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("new@mail.com")).thenReturn(false);
        when(userRepository.existsByUsername("new")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded");

        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(new UserDTO(null, "new", "new@mail.com", null, null, null));

        UserDTO result = userService.updateUser(auth, request);

        assertEquals("new@mail.com", result.email());
    }

    @Test
    void should_throw_if_update_email_exists() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        User user = new User();
        user.setEmail("test@mail.com");

        UpdateUserRequest request = new UpdateUserRequest("new", "new@mail.com", null);

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("new@mail.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.updateUser(auth, request));
    }

    @Test
    void should_update_without_password() {

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("test@mail.com");

        User user = new User();

        UpdateUserRequest request = new UpdateUserRequest("new", "new@mail.com", "");

        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByUsername(any())).thenReturn(false);

        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(new UserDTO(null, "new", "new@mail.com", null, null, null));

        UserDTO result = userService.updateUser(auth, request);

        assertEquals("new@mail.com", result.email());
    }
}