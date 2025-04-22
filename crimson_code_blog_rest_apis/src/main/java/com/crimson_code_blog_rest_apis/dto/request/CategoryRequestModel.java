package com.crimson_code_blog_rest_apis.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CategoryRequestModel {

	@NotBlank(message = "Category name cannot be empty")
	String name;
	
	@NotBlank(message = "Category decription cannot be empty")
	String description;

	public CategoryRequestModel() {
		
	}

	public CategoryRequestModel(String name,String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
