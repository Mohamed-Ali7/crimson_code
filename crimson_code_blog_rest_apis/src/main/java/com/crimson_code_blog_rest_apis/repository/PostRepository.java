package com.crimson_code_blog_rest_apis.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.crimson_code_blog_rest_apis.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

	Page<PostEntity> findAllByUserId(long userId, Pageable pageable);
	Page<PostEntity> findAllByCategoryId(long categoryId, Pageable pageable);
}
