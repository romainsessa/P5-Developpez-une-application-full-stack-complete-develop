package com.openclassrooms.mddapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.payload.request.LoginRequest;
import com.openclassrooms.mddapi.payload.request.RegisterRequest;
import com.openclassrooms.mddapi.payload.response.TokenResponse;
import com.openclassrooms.mddapi.security.service.JwtService;
import com.openclassrooms.mddapi.service.impl.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private JwtService jwtService;
	private UserService userService;
	private AuthenticationManager authenticationManager;

	
	public AuthController(
			JwtService jwtService,
			UserService userService,
			AuthenticationManager authenticationManager) {
		this.userService = userService;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}
	
	@PostMapping("/login")
	public ResponseEntity<TokenResponse> authenticateUser(@Valid @RequestBody LoginRequest request) {
		String email = request.getIdentifier();
		String password = request.getPassword();
		try {
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					email, password);
			Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
			String token = jwtService.generateToken(authentication.getName());
			return ResponseEntity.ok(new TokenResponse(token));
		} catch (BadCredentialsException ex) {
			ex.printStackTrace();
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@PostMapping("/register")
	public ResponseEntity<TokenResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
		UserDTO user = userService.createUser(request);
		String token = jwtService.generateToken(user.username());
		return ResponseEntity.ok(new TokenResponse(token));
	}
}
