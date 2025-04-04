package com.usecom.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.usecom.response.ApiResponse;

@RestController
public class HomeController {
	@GetMapping("/")
	public ApiResponse homeConntrollerHandler() {
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage("Welcome to e commerce multivendor webdite!");
		return apiResponse;
		}
}
 