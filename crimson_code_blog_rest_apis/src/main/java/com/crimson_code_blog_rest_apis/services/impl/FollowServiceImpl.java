package com.crimson_code_blog_rest_apis.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.crimson_code_blog_rest_apis.dto.response.FollowingStatusResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.PageResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.UserResponseModel;
import com.crimson_code_blog_rest_apis.entity.UserEntity;
import com.crimson_code_blog_rest_apis.exceptions.CrimsonCodeGlobalException;
import com.crimson_code_blog_rest_apis.exceptions.ResourceNotFoundException;
import com.crimson_code_blog_rest_apis.repository.UserRepository;
import com.crimson_code_blog_rest_apis.security.UserPrincipal;
import com.crimson_code_blog_rest_apis.services.FollowService;

import jakarta.transaction.Transactional;

@Service
public class FollowServiceImpl implements FollowService {
	
	private UserRepository userRepository;
	private ModelMapper modelMapper;
	
	@Autowired
	public FollowServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public void follow(String targetUserId, UserPrincipal authenticatedUser) {

		UserEntity userToFollow = userRepository.findByPublicId(targetUserId).
				orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + targetUserId));

		String currentUserId = authenticatedUser.getPublicId();
		UserEntity currentUser = userRepository.findByPublicId(currentUserId).
				orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + currentUserId));
		
		if (currentUser.equals(userToFollow)) {
			throw new CrimsonCodeGlobalException("You cannot follow yourself.");
		}
		
		boolean isFollowing = userRepository.isFollowing(currentUser.getId(), userToFollow.getId());
		
		if (isFollowing) {
			throw new CrimsonCodeGlobalException("You are already following this user.");
		}
		
		currentUser.addFollowing(userToFollow);
		userRepository.save(currentUser);
	}
	

	@Override
	@Transactional
	public void unFollow(String targetUserId, UserPrincipal authenticatedUser) {
		UserEntity userToFollow = userRepository.findByPublicId(targetUserId).
				orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + targetUserId));

		String currentUserId = authenticatedUser.getPublicId();
		UserEntity currentUser = userRepository.findByPublicId(currentUserId).
				orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + currentUserId));
		
		userRepository.unfollowUser(currentUser.getId(), userToFollow.getId());

	}

	@Override
	public PageResponseModel<UserResponseModel> followers(String targetUserId, int page, int size,
			String sortBy, String sortDir) {
		
		UserEntity userToFollow = userRepository.findByPublicId(targetUserId).
				orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + targetUserId));

		Sort sort = sortDir.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = PageRequest.of(page, size, sort);
		
		Page<UserEntity> followersPage = userRepository.findUserFollowers(userToFollow.getId(), pageable);
		
		List<UserEntity> followers = followersPage.getContent();
		
		List<UserResponseModel> followersResponse = followers.stream()
				.map(follower -> modelMapper.map(follower, UserResponseModel.class)).collect(Collectors.toList());
		
		PageResponseModel<UserResponseModel> pageResponse = new PageResponseModel<>();
		
		pageResponse.setContent(followersResponse);
		pageResponse.setPageNumber(++page);
		pageResponse.setPageSize(followersPage.getNumberOfElements());
		pageResponse.setTotalElements(followersPage.getTotalElements());
		pageResponse.setTotalPages(followersPage.getTotalPages());
		pageResponse.setIsLast(followersPage.isLast());
		
		return pageResponse;
	}

	@Override
	public PageResponseModel<UserResponseModel> following(String targetUserId, int page, int size,
			String sortBy, String sortDir) {
		UserEntity userToFollow = userRepository.findByPublicId(targetUserId).
				orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + targetUserId));

		Sort sort = sortDir.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = PageRequest.of(page, size, sort);
		
		Page<UserEntity> followingsPage = userRepository.findUserFollowings(userToFollow.getId(), pageable);
		
		List<UserEntity> followings = followingsPage.getContent();
		
		List<UserResponseModel> followingsResponse = followings.stream()
				.map(follower -> modelMapper.map(follower, UserResponseModel.class)).collect(Collectors.toList());
		
		PageResponseModel<UserResponseModel> pageResponse = new PageResponseModel<>();
		
		pageResponse.setContent(followingsResponse);
		pageResponse.setPageNumber(++page);
		pageResponse.setPageSize(followingsPage.getNumberOfElements());
		pageResponse.setTotalElements(followingsPage.getTotalElements());
		pageResponse.setTotalPages(followingsPage.getTotalPages());
		pageResponse.setIsLast(followingsPage.isLast());
		
		return pageResponse;
	}

	@Override
	public FollowingStatusResponseModel followingStatus(String targetUserId, UserPrincipal authenticatedUser) {

		
		UserEntity userToFollow = userRepository.findByPublicId(targetUserId).
				orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + targetUserId));

		if (authenticatedUser == null || authenticatedUser.getUserEntity() == null) {
		    throw new AccessDeniedException("Full authentication is required to access this resource");
		}

		UserEntity currentUser = authenticatedUser.getUserEntity();

		boolean isFollowing = userRepository.isFollowing(currentUser.getId(), userToFollow.getId());
		
		return new FollowingStatusResponseModel(isFollowing);
	}

}
