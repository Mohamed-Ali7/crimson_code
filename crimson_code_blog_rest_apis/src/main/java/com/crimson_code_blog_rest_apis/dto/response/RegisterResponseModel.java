package com.crimson_code_blog_rest_apis.dto.response;

import java.time.LocalDateTime;

public class RegisterResponseModel {
	private String publicId;
	private String email;
	private String firstName;
	private String lastName;
	private LocalDateTime joinedAt;

	public RegisterResponseModel() {

	}

	public RegisterResponseModel(String publicId, String email, String firstName, String lastName,
			LocalDateTime joinedAt) {
		super();
		this.publicId = publicId;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.joinedAt = joinedAt;
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDateTime getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(LocalDateTime joinedAt) {
		this.joinedAt = joinedAt;
	}
	
}
