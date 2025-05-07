package com.crimson_code_blog_rest_apis.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crimson_code_blog_rest_apis.entity.PostEntity;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

	
	@Override
	@EntityGraph(attributePaths = {"user", "category", "tags"})
	Page<PostEntity> findAll(Pageable pageable);
	
	Page<PostEntity> findAllByUserId(long userId, Pageable pageable);
	Page<PostEntity> findAllByCategoryId(long categoryId, Pageable pageable);
	
	//@Query("SELECT post FROM PostEntity post JOIN post.tags tag WHERE tag.id = :tagId") // using JPQL Query
	Page<PostEntity> findByTags_Id(long tagId, Pageable pageable);
	
	@Query("SELECT DISTINCT post FROM PostEntity post LEFT JOIN post.tags tag WHERE post.title LIKE %:title% OR tag.name IN :tags")
	Page<PostEntity> searchByTitleOrTags(@Param("title") String title, @Param("tags") List<String> tags, Pageable pageable);
	
	
	@Query("SELECT DISTINCT post FROM PostEntity post LEFT JOIN post.tags tag WHERE tag.name IN :tags")
	Page<PostEntity> searchByTags(@Param("tags") List<String> tags, Pageable pageable);
	
}
