package com.crimson_code_blog_rest_apis.dto.response;

import java.time.OffsetDateTime;
import java.util.List;

public class PostResponseModel {
	private long id;
	private String title;
	private String content;
	private String imageUrl;
	private long categoryId;
	private String userPublicId;
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	private List<TagResponseModel> tags;
	
	public PostResponseModel() {
		
	}

	public PostResponseModel(long id, String title, String content, String imageUrl, long categoryId, String userPublicId,
			OffsetDateTime createdAt, OffsetDateTime updatedAt, List<TagResponseModel> tags) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.imageUrl = imageUrl;
		this.userPublicId = userPublicId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.tags = tags;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public String getUserPublicId() {
		return userPublicId;
	}

	public void setUserPublicId(String userPublicId) {
		this.userPublicId = userPublicId;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(OffsetDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public List<TagResponseModel> getTags() {
		return tags;
	}

	public void setTags(List<TagResponseModel> tags) {
		this.tags = tags;
	}
}
