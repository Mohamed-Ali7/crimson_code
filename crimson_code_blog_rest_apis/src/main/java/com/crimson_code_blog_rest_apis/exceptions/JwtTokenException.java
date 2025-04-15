package com.crimson_code_blog_rest_apis.exceptions;

import org.springframework.http.HttpStatus;

public class JwtTokenException extends RuntimeException {

	private String tokenType;

	public JwtTokenException(String tokenType , String message) {
		super(message);
		this.tokenType = tokenType;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
}
