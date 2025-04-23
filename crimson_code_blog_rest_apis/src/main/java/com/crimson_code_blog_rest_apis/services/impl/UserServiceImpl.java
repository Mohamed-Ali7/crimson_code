package com.crimson_code_blog_rest_apis.services.impl;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crimson_code_blog_rest_apis.dto.request.ChangePasswordRequestModel;
import com.crimson_code_blog_rest_apis.dto.request.PasswordResetConfirmationRequest;
import com.crimson_code_blog_rest_apis.dto.request.PasswordResetRequestModel;
import com.crimson_code_blog_rest_apis.dto.request.UpdateUserRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.PageResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.PostSummaryResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.UserResponseModel;
import com.crimson_code_blog_rest_apis.entity.PasswordResetTokenEntity;
import com.crimson_code_blog_rest_apis.entity.PostEntity;
import com.crimson_code_blog_rest_apis.entity.UserEntity;
import com.crimson_code_blog_rest_apis.exceptions.CrimsonCodeGlobalException;
import com.crimson_code_blog_rest_apis.exceptions.JwtTokenException;
import com.crimson_code_blog_rest_apis.exceptions.ResourceNotFoundException;
import com.crimson_code_blog_rest_apis.repository.PasswordResetTokenRepository;
import com.crimson_code_blog_rest_apis.repository.PostRepository;
import com.crimson_code_blog_rest_apis.repository.UserRepository;
import com.crimson_code_blog_rest_apis.security.UserPrincipal;
import com.crimson_code_blog_rest_apis.services.EmailService;
import com.crimson_code_blog_rest_apis.services.UserService;
import com.crimson_code_blog_rest_apis.utils.JwtTokenType;
import com.crimson_code_blog_rest_apis.utils.JwtUtils;

@Service
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private ModelMapper modelMapper;
	private JwtUtils jwtUtils;
	private EmailService emailService;
	private PasswordResetTokenRepository passwordResetTokenRepository;
	private PasswordEncoder passwordEncoder;
	private PostRepository postRepository;
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, JwtUtils jwtUtils,
			EmailService emailService, PasswordResetTokenRepository passwordResetTokenRepository,
			PasswordEncoder passwordEncoder, PostRepository postRepository) {

		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.jwtUtils = jwtUtils;
		this.emailService = emailService;
		this.passwordResetTokenRepository = passwordResetTokenRepository;
		this.passwordEncoder = passwordEncoder;
		this.postRepository = postRepository;
	}

	@Override
	public UserResponseModel getCurrentUser(UserPrincipal userPrincipal) {
		if (userPrincipal == null || userPrincipal.getUserEntity() == null) {
		    throw new CrimsonCodeGlobalException("Authenticated user not found");
		}
		return modelMapper.map(userPrincipal.getUserEntity(), UserResponseModel.class);
	}

	@Override
	public UserResponseModel getUser(String publicId) {

		UserEntity userEntity = userRepository.findByPublicId(publicId)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + publicId));
		
		UserResponseModel userResponse = modelMapper.map(userEntity, UserResponseModel.class);

		return userResponse;
	}

	@Override
	public PageResponseModel<UserResponseModel> getAllUser(int page, int pageSize, String sortBy, String sortDir) {

		page = page > 0 ? page - 1 : page; // To make pages start from 1 not 0 as it's more user-friendly
		
		Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable userPageable = PageRequest.of(page, pageSize, sort);
		
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

	@Override
	public void passwordResetRequest(PasswordResetRequestModel passwordResetRequest) {
		String userEmail = passwordResetRequest.getEmail();
		
		UserEntity user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with email: " + userEmail));
		
		Optional <PasswordResetTokenEntity> oldPasswordResetToken =
				passwordResetTokenRepository.findByUserId(user.getId());
		
		if (oldPasswordResetToken.isPresent()) {
			passwordResetTokenRepository.delete(oldPasswordResetToken.get());
		}
		
		String passwordResetToken = jwtUtils.generatePasswordResetToken(userEmail);
		
		PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
		
		passwordResetTokenEntity.setToken(passwordResetToken);
		passwordResetTokenEntity.setUser(user);

		emailService.sendPasswordResetEmail(userEmail, passwordResetToken);

		passwordResetTokenRepository.save(passwordResetTokenEntity);
		
	}

	@Override
	public void resetPassword(PasswordResetConfirmationRequest passwordResetConfirmation) {

		String passwordResetToken = passwordResetConfirmation.getToken();

		jwtUtils.validateJwtToken(passwordResetToken, JwtTokenType.PASSWORD_RESET_TOKEN);
		
		PasswordResetTokenEntity passwordResetTokenEntity = 
				passwordResetTokenRepository.findByToken(passwordResetToken)
				.orElseThrow(() -> 
				new JwtTokenException(JwtTokenType.PASSWORD_RESET_TOKEN, "Invalid Password reset token")
				);
		
		String newPassword = passwordResetConfirmation.getNewPassword();
		String confirmPassword = passwordResetConfirmation.getConfirmPassword();
		
		if (!newPassword.equals(confirmPassword)) {
			throw new CrimsonCodeGlobalException("The new password and confirm password do not match");
		}
		
		UserEntity user = passwordResetTokenEntity.getUser();
		
		user.setPassword(passwordEncoder.encode(newPassword));
		
		userRepository.save(user);
		
		passwordResetTokenRepository.delete(passwordResetTokenEntity);
		
	}

	@Override
	public void changePassword(UserPrincipal userPrincipal, ChangePasswordRequestModel changePasswordRequest) {

		String currentPassword = changePasswordRequest.getCurrentPassword();
		String newPassword = changePasswordRequest.getNewPassword();
		String confirmPassword = changePasswordRequest.getConfirmPassword();
		
		if (!newPassword.equals(confirmPassword)) {
			throw new CrimsonCodeGlobalException("The new password and confirm password do not match");
		}
		
		UserEntity user = userPrincipal.getUserEntity();
		
		if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
			throw new CrimsonCodeGlobalException("Current password is incorrect.");
		}
		
		if (currentPassword.equals(newPassword)) {
			return;
		}
		
		user.setPassword(passwordEncoder.encode(newPassword));
		
		userRepository.save(user);
	}

	@Override
	public PageResponseModel<PostSummaryResponseModel> getUserPosts(String publicId, int page, int pageSize, String sortBy,
			String sortDir) {

		UserEntity userEntity = userRepository.findByPublicId(publicId)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + publicId));
		
		page = page > 0 ? page - 1 : page; // To make pages start from 1 not 0 as it's more user-friendly
		
		Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = PageRequest.of(page, pageSize, sort);
		
		Page<PostEntity> userPostsPage = postRepository.findAllByUserId(userEntity.getId(), pageable);
		
		List<PostEntity> userPosts = userPostsPage.getContent();
		
		List<PostSummaryResponseModel> userPostsResponse = userPosts.stream()
				.map(post -> PostServiceImpl.mapToPostResponse(new PostSummaryResponseModel(), post))
				.collect(Collectors.toList());
		
		PageResponseModel<PostSummaryResponseModel> pageResponse = new PageResponseModel<>();
		
		pageResponse.setContent(userPostsResponse);
		pageResponse.setPageNumber(++page);
		pageResponse.setPageSize(userPostsPage.getNumberOfElements());
		pageResponse.setTotalElements(userPostsPage.getTotalElements());
		pageResponse.setTotalPages(userPostsPage.getTotalPages());
		pageResponse.setIsLast(userPostsPage.isLast());
		
		return pageResponse;
	}

}
