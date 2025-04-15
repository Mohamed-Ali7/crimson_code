package com.crimson_code_blog_rest_apis.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CrimsonCodeGlobalException.class)
	public ResponseEntity<ErrorResponse> handleCrimsonCodeGlobalException(
			CrimsonCodeGlobalException ex, HttpServletRequest request) {
		
		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(), ex.getMessage(), request.getRequestURI());
		
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(AccessRefreshTokenException.class)
	public ResponseEntity<ErrorResponse> handleAccessRefreshTokenException(
			AccessRefreshTokenException ex, HttpServletRequest request) {

		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
				HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), request.getRequestURI());
		
		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleAccessRefreshTokenException(
			ResourceNotFoundException ex, HttpServletRequest request) {

		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(), ex.getMessage(), request.getRequestURI());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}
	
}
