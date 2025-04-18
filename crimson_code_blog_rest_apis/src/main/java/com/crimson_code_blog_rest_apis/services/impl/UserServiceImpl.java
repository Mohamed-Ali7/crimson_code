package com.crimson_code_blog_rest_apis.services.impl;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.crimson_code_blog_rest_apis.dto.request.UpdateUserRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.PageResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.UserResponseModel;
import com.crimson_code_blog_rest_apis.entity.UserEntity;
import com.crimson_code_blog_rest_apis.exceptions.CrimsonCodeGlobalException;
import com.crimson_code_blog_rest_apis.exceptions.ResourceNotFoundException;
import com.crimson_code_blog_rest_apis.repository.UserRepository;
import com.crimson_code_blog_rest_apis.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private ModelMapper modelMapper;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public UserResponseModel getUser(String publicId) {

		UserEntity userEntity = userRepository.findByPublicId(publicId)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + publicId));
		
		UserResponseModel userResponse = modelMapper.map(userEntity, UserResponseModel.class);

		return userResponse;
	}

	@Override
	public PageResponseModel<UserResponseModel> getAllUser(int page, int PageSize, String sortBy, String sortDir) {

		page = page > 0 ? page - 1 : page; // To make pages start from 1 not 0 as it's more user-friendly
		
		Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable userPageable = PageRequest.of(page, PageSize, sort);
		
		Page<UserEntity> usersPage = userRepository.findAll(userPageable);
		
		List<UserEntity> users = usersPage.getContent();
		
		Type typeList = new TypeToken<List<UserResponseModel>>() {}.getType();
		
		List<UserResponseModel> usersResponse = modelMapper.map(users, typeList);
		
		PageResponseModel<UserResponseModel> pageResponse = new PageResponseModel<>();
		
		pageResponse.setContent(usersResponse);
		pageResponse.setPageNumber(++page);
		pageResponse.setPageSize(usersPage.getNumberOfElements());
		pageResponse.setTotalElements(usersPage.getTotalElements());
		pageResponse.setTotalPages(usersPage.getTotalPages());
		pageResponse.setIsLast(usersPage.isLast());
		
		return pageResponse;
	}

	@Override
	public UserResponseModel updateUser(String publicId, UpdateUserRequestModel updateRequest) {

		UserEntity userEntity = userRepository.findByPublicId(publicId)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + publicId));
		
		userEntity.setFirstName(updateRequest.getFirstName());
		userEntity.setLastName(updateRequest.getLastName());
		
		UserEntity updatedUser = userRepository.save(userEntity);
		
		UserResponseModel userResponse = modelMapper.map(updatedUser, UserResponseModel.class);
		
		return userResponse;
	}

	@Override
	public void deleteUser(String publicId) {

		UserEntity userEntity = userRepository.findByPublicId(publicId)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + publicId));
		
		userEntity.getRoles().forEach(role -> {
			if(role.getName().equals("ROLE_ADMIN")) {
				throw new CrimsonCodeGlobalException("Users with ADMIN role can't be deleted");
			}
		});
		
		userRepository.delete(userEntity);
		
	}

}
