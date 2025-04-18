package com.crimson_code_blog_rest_apis.services;

import com.crimson_code_blog_rest_apis.dto.request.UpdateUserRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.PageResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.UserResponseModel;

public interface UserService {

	UserResponseModel getUser(String publicId);
	PageResponseModel<UserResponseModel> getAllUser(int page, int size, String sortBy, String sortDir);
	UserResponseModel updateUser(String publicId, UpdateUserRequestModel updateRequest);
	void deleteUser(String publicId);
}
