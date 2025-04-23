package com.crimson_code_blog_rest_apis.services.impl;

import com.crimson_code_blog_rest_apis.dto.request.TagRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.PageResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.PostResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.TagResponseModel;
import com.crimson_code_blog_rest_apis.services.TagService;

public class TagServiceImpl implements TagService {

	@Override
	public TagResponseModel createTag(TagRequestModel tagRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TagResponseModel getTag(long tagId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResponseModel<TagResponseModel> getAllTags(int page, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TagResponseModel updateTag(long tagId, TagRequestModel tagRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteTag(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public PageResponseModel<PostResponseModel> getTagPosts(long tagId, int page, int pageSize, String sortBy,
			String sortDir) {
		// TODO Auto-generated method stub
		return null;
	}

}
