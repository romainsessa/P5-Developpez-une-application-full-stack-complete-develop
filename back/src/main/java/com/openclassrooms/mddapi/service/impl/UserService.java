package com.openclassrooms.mddapi.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.exception.BadRequestException;
import com.openclassrooms.mddapi.exception.NotFoundException;
import com.openclassrooms.mddapi.mapper.UserMapper;
import com.openclassrooms.mddapi.payload.request.UpdateUserRequest;
import com.openclassrooms.mddapi.repository.UserRepository;
import com.openclassrooms.mddapi.service.IUserService;

public class UserService implements IUserService {
	private UserRepository userRepository;
	private UserMapper userMapper;
	private PasswordEncoder passwordEncoder;

	public UserService(
			UserRepository userRepository, 
			UserMapper userMapper, 
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.passwordEncoder = passwordEncoder;
	}

	public UserDTO getUser(Authentication authentication) {
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("Utilisateur"));
		return userMapper.toDto(user);
	}

	public UserDTO updateUser(Authentication authentication, UpdateUserRequest userUpdateDTO) {
		String email = authentication.getName();
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException("Utilisateur"));

		if (userRepository.existsByEmail(userUpdateDTO.email())) {
			throw new BadRequestException("Email déjà utilisé");
		}
		if (userRepository.existsByUsername(userUpdateDTO.username())) {
			throw new BadRequestException("Nom d'utilisateur déjà utilisé");
		}

		user.setUsername(userUpdateDTO.username());
		user.setEmail(userUpdateDTO.email());

		if (userUpdateDTO.password() != null && !userUpdateDTO.password().isEmpty()) {
			user.setPassword(passwordEncoder.encode(userUpdateDTO.password()));
		}

		User updatedUser = userRepository.save(user);
		return userMapper.toDto(updatedUser);
	}
}