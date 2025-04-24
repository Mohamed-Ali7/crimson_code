package com.crimson_code_blog_rest_apis.services.impl;

import org.springframework.stereotype.Service;

import com.crimson_code_blog_rest_apis.dto.request.CommentRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.CommentResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.PageResponseModel;
import com.crimson_code_blog_rest_apis.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

	@Override
	public CommentResponseModel createComment(long postId, CommentRequestModel commentRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommentResponseModel getComment(long postId, long commentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResponseModel<CommentResponseModel> getAllComments(long postId, int page, int pageSize, String sortBy,
			String sortDir) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommentResponseModel updateComment(long postId, long commentId, CommentRequestModel commentRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteComment(long postId, long commentId) {
		// TODO Auto-generated method stub

	}

}
