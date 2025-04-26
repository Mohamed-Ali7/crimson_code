package com.crimson_code_blog_rest_apis.dto.response;

import java.time.OffsetDateTime;
import java.util.List;

public class PostDetailResponseModel extends PostResponse{

	private List<TagResponseModel> tags;

	public PostDetailResponseModel() {

	}

	public PostDetailResponseModel(long id, String title, String content, String imageUrl, long categoryId,
			String userPublicId, OffsetDateTime createdAt, OffsetDateTime updatedAt, List<TagResponseModel> tags) {

		super(id, title,content, imageUrl, categoryId, userPublicId, createdAt, updatedAt);
		this.tags = tags;
	}

	public List<TagResponseModel> getTags() {
		return tags;
	}

	public void setTags(List<TagResponseModel> tags) {
		this.tags = tags;
	}
	
}
