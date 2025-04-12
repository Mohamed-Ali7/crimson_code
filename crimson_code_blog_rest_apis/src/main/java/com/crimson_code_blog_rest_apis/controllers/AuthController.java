package com.crimson_code_blog_rest_apis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crimson_code_blog_rest_apis.dto.request.RegisterRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.RegisterResponseModel;
import com.crimson_code_blog_rest_apis.services.AuthService;

@RestController()
@RequestMapping("/api/auth")
public class AuthController {

	private AuthService authService;
	
	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}
	
	@PostMapping("/register")
	ResponseEntity<RegisterResponseModel> register(@RequestBody RegisterRequestModel registerRequest) {
		
		return new ResponseEntity<>(authService.register(registerRequest), HttpStatus.CREATED);
	}
}
