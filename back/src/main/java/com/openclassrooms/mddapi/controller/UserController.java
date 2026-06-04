package com.openclassrooms.mddapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.payload.request.UpdateUserRequest;
import com.openclassrooms.mddapi.service.IUserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	private final IUserService userService;

	public UserController(IUserService userService) {
		this.userService = userService;
	}

	@GetMapping("/me")
	public ResponseEntity<UserDTO> getUser(Authentication authentication) {
		UserDTO userDTO = userService.getUser(authentication);
		return ResponseEntity.ok(userDTO);
	}

	@PutMapping("/me")
	public ResponseEntity<UserDTO> updateUser(Authentication authentication, @RequestBody UpdateUserRequest userUpdateDTO) {
		UserDTO updatedUser = userService.updateUser(authentication, userUpdateDTO);
		return ResponseEntity.ok(updatedUser);
	}
}
