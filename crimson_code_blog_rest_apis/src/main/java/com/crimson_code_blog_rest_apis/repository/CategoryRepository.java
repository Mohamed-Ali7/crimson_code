package com.crimson_code_blog_rest_apis.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.crimson_code_blog_rest_apis.entity.CategoryEntity;
import com.crimson_code_blog_rest_apis.entity.PostEntity;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

	Optional<CategoryEntity> findByName(String name);
}
