package com.crimson_code_blog_rest_apis.services;

import com.crimson_code_blog_rest_apis.dto.response.FollowingStatusResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.PageResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.UserResponseModel;
import com.crimson_code_blog_rest_apis.security.UserPrincipal;

public interface FollowService {

	void follow(String targetUserId, UserPrincipal authenticatedUser);
	void unFollow(String targetUserId, UserPrincipal authenticatedUser);
	PageResponseModel<UserResponseModel> followers(String targetUserId, int page, int size,
			String sortBy, String sortDir);
	PageResponseModel<UserResponseModel> following(String targetUserId, int page, int size,
			String sortBy, String sortDir);
	FollowingStatusResponseModel followingStatus(String targetUserId, UserPrincipal authenticatedUser);
}
