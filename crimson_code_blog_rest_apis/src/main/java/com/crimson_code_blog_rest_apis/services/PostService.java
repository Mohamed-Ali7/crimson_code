package com.crimson_code_blog_rest_apis.services;

import org.springframework.web.multipart.MultipartFile;

import com.crimson_code_blog_rest_apis.dto.request.PostRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.PageResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.PostDetailResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.PostSummaryResponseModel;
import com.crimson_code_blog_rest_apis.security.UserPrincipal;

public interface PostService {

	PostDetailResponseModel createPost(PostRequestModel postRequest, MultipartFile postImage);
	PostDetailResponseModel getPost(long postId);
	PageResponseModel<PostSummaryResponseModel> getAllPosts(int page, int pageSize, String sortBy, String sortDir);
	PostDetailResponseModel updatePost(long postId, PostRequestModel postRequest, UserPrincipal userPrincipal);
	void deletePost(long postId, UserPrincipal userPrincipal);
}
