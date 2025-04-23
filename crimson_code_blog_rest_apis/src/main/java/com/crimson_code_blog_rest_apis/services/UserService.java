package com.crimson_code_blog_rest_apis.services;

import com.crimson_code_blog_rest_apis.dto.request.ChangePasswordRequestModel;
import com.crimson_code_blog_rest_apis.dto.request.PasswordResetConfirmationRequest;
import com.crimson_code_blog_rest_apis.dto.request.PasswordResetRequestModel;
import com.crimson_code_blog_rest_apis.dto.request.UpdateUserRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.PageResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.PostSummaryResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.UserResponseModel;
import com.crimson_code_blog_rest_apis.security.UserPrincipal;

import jakarta.validation.Valid;

public interface UserService {

	UserResponseModel getCurrentUser(UserPrincipal userPrincipal);
	UserResponseModel getUser(String publicId);
	PageResponseModel<UserResponseModel> getAllUser(int page, int size, String sortBy, String sortDir);
	UserResponseModel updateUser(String publicId, UpdateUserRequestModel updateRequest);
	void deleteUser(String publicId);
	void passwordResetRequest(PasswordResetRequestModel passwordResetRequest);
	void resetPassword(PasswordResetConfirmationRequest passwordResetConfirmation);
	void changePassword(UserPrincipal userPrincipal, ChangePasswordRequestModel changePasswordRequest);
	PageResponseModel<PostSummaryResponseModel> getUserPosts(String publicId, int page, int pageSize,
			String sortBy, String sortDir);
}
