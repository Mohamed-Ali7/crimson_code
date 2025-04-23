package com.crimson_code_blog_rest_apis.dto.request;

public class TagRequestModel {

	private String name;
	
	public TagRequestModel() {
		
	}

	public TagRequestModel(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
