package com.crimson_code_blog_rest_apis.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crimson_code_blog_rest_apis.entity.TagEntity;

public interface TagRepository extends JpaRepository<TagEntity, Long> {

	Optional<TagEntity> findByName(String name);
}
