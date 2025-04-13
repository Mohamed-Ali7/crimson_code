package com.crimson_code_blog_rest_apis.services;

import com.crimson_code_blog_rest_apis.dto.request.RegisterRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.RegisterResponseModel;

public interface AuthService {

	RegisterResponseModel register(RegisterRequestModel registerRequest);
}
