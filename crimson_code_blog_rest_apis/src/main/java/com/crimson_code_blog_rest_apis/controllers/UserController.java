package com.crimson_code_blog_rest_apis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crimson_code_blog_rest_apis.dto.request.UpdateUserRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.OperationStatusResponse;
import com.crimson_code_blog_rest_apis.dto.response.PageResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.UserResponseModel;
import com.crimson_code_blog_rest_apis.services.UserService;
import com.crimson_code_blog_rest_apis.utils.OperationName;
import com.crimson_code_blog_rest_apis.utils.OperationStatus;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@GetMapping("/{publicId}")
	public ResponseEntity<UserResponseModel> getUser(@PathVariable String publicId) {
		return new ResponseEntity<>(userService.getUser(publicId), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<PageResponseModel<UserResponseModel>> getAllUsers(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "15") int pageSize,
			@RequestParam(name = "sort_by", defaultValue = "id") String sortBy,
			@RequestParam(name = "sort_dir", defaultValue = "asc") String sortDir
			){
		
		return new ResponseEntity<>(userService.getAllUser(page, pageSize, sortBy, sortDir), HttpStatus.OK);
	}
	
	@PreAuthorize("principal.publicId == #publicId")
	@PutMapping("/{publicId}")
	public ResponseEntity<UserResponseModel> updateUser(@PathVariable String publicId,
			@RequestBody UpdateUserRequestModel updateRequest) {
		
		return new ResponseEntity<>(userService.updateUser(publicId, updateRequest), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN') or principal.publicId == #publicId")
	@DeleteMapping("/{publicId}")
	public OperationStatusResponse deleteUser(@PathVariable String publicId) {
		
		OperationStatusResponse operationStatus = new OperationStatusResponse();
		
		userService.deleteUser(publicId);
		
		operationStatus.setOperationName(OperationName.DELETE_USER.name());
		
		operationStatus.setOperationStatus(OperationStatus.SUCCESS.name());
		
		operationStatus.setMessage("The user has been deleted successfully");
		
		return operationStatus;
	}
	
}
