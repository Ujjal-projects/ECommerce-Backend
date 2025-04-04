package com.usecom.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usecom.domain.USER_ROLE;
import com.usecom.repository.UserRepository;
import com.usecom.response.AuthResponse;
import com.usecom.response.SignUpRequest;
import com.usecom.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final UserRepository userRepository;
	private final AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignUpRequest req) throws Exception{
		String jwt = authService.createUser(req);
		
		AuthResponse response = new AuthResponse();
		response.setJwt(jwt);
		response.setMessage("register success");
		response.setRole(USER_ROLE.ROLE_CUSTOMER);
		return ResponseEntity.ok(response);
	}
}
