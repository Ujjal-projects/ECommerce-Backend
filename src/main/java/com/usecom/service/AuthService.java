package com.usecom.service;

import com.usecom.request.LoginRequest;
import com.usecom.response.AuthResponse;
import com.usecom.response.SignUpRequest;

public interface AuthService {
	void sendLoginAndSignupOtp(String email) throws Exception;
	String createUser(SignUpRequest req) throws Exception;
	AuthResponse singing(LoginRequest req);
	
}
