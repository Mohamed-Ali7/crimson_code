package com.crimson_code_blog_rest_apis.services.impl;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.crimson_code_blog_rest_apis.dto.request.PostRequestModel;
import com.crimson_code_blog_rest_apis.dto.response.PageResponseModel;
import com.crimson_code_blog_rest_apis.dto.response.PostResponseModel;
import com.crimson_code_blog_rest_apis.entity.CategoryEntity;
import com.crimson_code_blog_rest_apis.entity.PostEntity;
import com.crimson_code_blog_rest_apis.entity.UserEntity;
import com.crimson_code_blog_rest_apis.exceptions.ResourceNotFoundException;
import com.crimson_code_blog_rest_apis.repository.CategoryRepository;
import com.crimson_code_blog_rest_apis.repository.PostRepository;
import com.crimson_code_blog_rest_apis.repository.UserRepository;
import com.crimson_code_blog_rest_apis.security.UserPrincipal;
import com.crimson_code_blog_rest_apis.services.PostService;
import com.crimson_code_blog_rest_apis.utils.UserRoles;

@Service
public class PostServiceImpl implements PostService {
	
	private PostRepository postRepository;
	private UserRepository userRepository;
	private CategoryRepository categoryRepository;

	@Autowired
	public PostServiceImpl(PostRepository postRepository, UserRepository userRepository,
			CategoryRepository categoryRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public PostResponseModel createPost(PostRequestModel postRequest) {
		
		String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		
		UserEntity userEntity = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with email: " + userEmail));
	
		CategoryEntity categoryEntity = categoryRepository.findById(postRequest.getCategoryId())
				.orElseThrow(() -> new ResourceNotFoundException(
						"Category does not exist with id: " + postRequest.getCategoryId()));
		
		PostEntity newPost = new PostEntity();
		
		OffsetDateTime postCreationDate = OffsetDateTime.now(ZoneOffset.UTC);
		
		newPost.setTitle(postRequest.getTitle());
		newPost.setContent(postRequest.getContent());
		newPost.setCreatedAt(postCreationDate);
		newPost.setUpdatedAt(postCreationDate);
		newPost.setUserPublicId(userEntity.getPublicId());
		newPost.setUser(userEntity);
		newPost.setCategory(categoryEntity);
		
		PostEntity savedPost = postRepository.save(newPost);
		
		PostResponseModel postResponse = mapToPostResponse(savedPost);
		
		return postResponse;
	}

	@Override
	public PostResponseModel getPost(long postId) {

		PostEntity postEntity = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post does not exist with id: " + postId));
		
		PostResponseModel postResponse = mapToPostResponse(postEntity);
		return postResponse;
	}

	@Override
	public PageResponseModel<PostResponseModel> getAllPosts(int page, int pageSize, String sortBy, String sortDir) {
		page = page > 0 ? page - 1 : page; // To make pages start from 1 not 0 as it's more user-friendly
		
		Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
		
		Pageable pageable = PageRequest.of(page, pageSize, sort);
		
		Page<PostEntity> postsPage = postRepository.findAll(pageable);
		
		List<PostEntity> posts = postsPage.getContent();
		
		List<PostResponseModel> postsResponse = posts.stream()
				.map(post -> mapToPostResponse(post)).collect(Collectors.toList());
		
		PageResponseModel<PostResponseModel> pageResponse = new PageResponseModel<>();
		
		pageResponse.setContent(postsResponse);
		pageResponse.setPageNumber(++page);
		pageResponse.setPageSize(postsPage.getNumberOfElements());
		pageResponse.setTotalElements(postsPage.getTotalElements());
		pageResponse.setTotalPages(postsPage.getTotalPages());
		pageResponse.setIsLast(postsPage.isLast());
		
		return pageResponse;
	}

	@Override
	public PostResponseModel updatePost(long postId, PostRequestModel postRequest, UserPrincipal userPrincipal) {
		PostEntity postEntity = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post does not exist with id: " + postId));
		
		boolean isAdmin = userPrincipal.getAuthorities()
				.contains(new SimpleGrantedAuthority(UserRoles.ROLE_ADMIN.name()));
		
		
		if (!userPrincipal.getPublicId().equals(postEntity.getUserPublicId()) && !isAdmin) {
			throw new AccessDeniedException(
					"UNAUTHORIZED: User " + userPrincipal.getUsername() + " is not authorized to update this post");
		}
		
		postEntity.setTitle(postRequest.getTitle());
		postEntity.setContent(postRequest.getContent());
		
		if (postEntity.getCategory().getId() != postRequest.getCategoryId()) {
			CategoryEntity categoryEntity = categoryRepository.findById(postRequest.getCategoryId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Category does not exist with id: " + postRequest.getCategoryId()));
			
			postEntity.setCategory(categoryEntity);
		}
		postEntity.setUpdatedAt(OffsetDateTime.now(ZoneOffset.UTC));
		
		PostEntity updatedPost = postRepository.save(postEntity);
		PostResponseModel postResponse = mapToPostResponse(updatedPost);
		
		return postResponse;
	}

	@Override
	public void deletePost(long postId, UserPrincipal userPrincipal) {
		PostEntity postEntity = postRepository.findById(postId)
				.orElseThrow(() -> new ResourceNotFoundException("Post does not exist with id: " + postId));
		
		boolean isAdmin = userPrincipal.getAuthorities()
				.contains(new SimpleGrantedAuthority(UserRoles.ROLE_ADMIN.name()));
		
		
		if (!userPrincipal.getPublicId().equals(postEntity.getUserPublicId()) && !isAdmin) {
			throw new AccessDeniedException(
					"UNAUTHORIZED: User " + userPrincipal.getUsername() + " is not authorized to delete this post");
		}

		postRepository.delete(postEntity);
	}

	protected static PostResponseModel mapToPostResponse(PostEntity postEntity) {
		PostResponseModel postResponse = new PostResponseModel();
		
		postResponse.setId(postEntity.getId());
		postResponse.setTitle(postEntity.getTitle());
		postResponse.setContent(postEntity.getContent());
		postResponse.setUserPublicId(postEntity.getUserPublicId());
		postResponse.setCreatedAt(postEntity.getCreatedAt());
		postResponse.setUpdatedAt(postEntity.getUpdatedAt());
		postResponse.setCategoryId(postEntity.getCategory().getId());
				
		return postResponse;
	}
}
