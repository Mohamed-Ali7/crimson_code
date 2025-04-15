package com.crimson_code_blog_rest_apis.services;

import com.crimson_code_blog_rest_apis.dto.request.EmailVerificationRequest;
import com.crimson_code_blog_rest_apis.dto.request.LoginRequestModel;
import com.crimson_code_blog_rest_apis.dto.request.RegisterRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.LoginResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.OperationStatusResponse;
import com.crimson_code_blog_rest_apis.dto.response.RefreshTokenResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.RegisterResponseModel;

public interface AuthService {

	RegisterResponseModel register(RegisterRequestModel registerRequest);
	LoginResponseModel login(LoginRequestModel loginRequest);
	void emailVerification(String token);
	void emailVerificationRequest(EmailVerificationRequest verificationRequest);
	RefreshTokenResponseModel refreshAccessToken(String authorizationHeader);
}
