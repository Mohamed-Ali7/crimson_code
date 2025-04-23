package com.crimson_code_blog_rest_apis.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class PostRequestModel {

	@NotBlank(message = "Post title cannot be empty")
	private String title;
	
	@NotBlank(message = "Post content cannot be empty")
	// @Size(min = 20, message = "Post content must be at least 20 characters long.")
	private String content;
	
	@Positive(message = "Category id cannot be empty and must be greater than 0")
	private long categoryId;
	
	public PostRequestModel() {
		
	}

	public PostRequestModel(String title, String content, long categoryId) {
		this.title = title;
		this.content = content;
		this.categoryId = categoryId;
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

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
}