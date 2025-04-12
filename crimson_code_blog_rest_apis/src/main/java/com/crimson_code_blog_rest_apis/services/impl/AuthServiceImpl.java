package com.crimson_code_blog_rest_apis.services.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.management.relation.RoleResult;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crimson_code_blog_rest_apis.dto.request.RegisterRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.RegisterResponseModel;
import com.crimson_code_blog_rest_apis.entity.RoleEntity;
import com.crimson_code_blog_rest_apis.entity.UserEntity;
import com.crimson_code_blog_rest_apis.exceptions.CrimsonCodeGlobalException;
import com.crimson_code_blog_rest_apis.repository.RoleRepository;
import com.crimson_code_blog_rest_apis.repository.UserRepository;
import com.crimson_code_blog_rest_apis.services.AuthService;
import com.crimson_code_blog_rest_apis.utils.UserRoles;

@Service
public class AuthServiceImpl implements AuthService {

	private UserRepository userRepository;
	private ModelMapper modelMapper;
	private RoleRepository roleRepository;
	
	@Autowired
	public AuthServiceImpl(UserRepository userRepository, ModelMapper modelMapper, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.roleRepository = roleRepository;
	}


	@Override
	public RegisterResponseModel register(RegisterRequestModel registerRequest) {
		if(userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
			throw new CrimsonCodeGlobalException("This email already exists");
		}
		
		UserEntity newUser = modelMapper.map(registerRequest, UserEntity.class);
		
		newUser.setPublicId(UUID.randomUUID().toString());
		
		RoleEntity userRole = roleRepository.findByName(UserRoles.ROLE_USER.name())
				.orElseGet(() -> {
					RoleEntity newUserRole = new RoleEntity(UserRoles.ROLE_USER.name());
					return roleRepository.save(newUserRole);
				});
		
		newUser.addRole(userRole);
		newUser.setJoinedAt(LocalDateTime.now());
		
		UserEntity savedUser = userRepository.save(newUser);
		System.out.println(newUser.getJoinedAt());
		
		RegisterResponseModel registerResponse = modelMapper.map(savedUser, RegisterResponseModel.class);
		
		return registerResponse;
	}

}
