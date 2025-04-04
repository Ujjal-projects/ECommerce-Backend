package com.usecom.service;

import com.usecom.response.SignUpRequest;

public interface AuthService {
	String createUser(SignUpRequest req) throws Exception;
	
}
