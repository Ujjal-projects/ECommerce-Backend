package com.usecom.request;

import com.usecom.domain.USER_ROLE;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginOtpRequest {
	private String email;
	private String otp;
	private USER_ROLE role;
}
