package com.openclassrooms.mddapi.service;

import org.springframework.security.core.Authentication;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.payload.request.RegisterRequest;
import com.openclassrooms.mddapi.payload.request.UpdateUserRequest;

public interface IUserService {

	public UserDTO getUser(Authentication authentication);

	public UserDTO updateUser(Authentication authentication, UpdateUserRequest userUpdateDTO);
	
	public UserDTO createUser(RegisterRequest registerDTO);
}
