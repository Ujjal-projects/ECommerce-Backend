package com.usecom.controller;

import java.net.Authenticator.RequestorType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usecom.entity.VarificationCode;
import com.usecom.repository.VarificationCodeRepository;
import com.usecom.request.LoginOtpRequest;
import com.usecom.request.LoginRequest;
import com.usecom.response.ApiResponse;
import com.usecom.response.AuthResponse;
import com.usecom.service.AuthService;
import com.usecom.service.SellerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {
	private final SellerService sellerService;
	private final VarificationCodeRepository varificationCodeRepository;
	private final AuthService authService;
	 

	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> loginSeller(
			@RequestBody LoginRequest req
			) throws Exception{
		String otp = req.getOtp();
		String email = req.getEmail();

		req.setEmail("seller_" + email);
		AuthResponse authResponse = authService.singing(req);
		return ResponseEntity.ok(authResponse);
	}
	
}
