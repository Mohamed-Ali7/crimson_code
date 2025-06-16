package com.crimson_code_blog_rest_apis.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.crimson_code_blog_rest_apis.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	@EntityGraph(attributePaths = "roles")
	Optional<UserEntity> findByEmail(String email);

	Optional<UserEntity> findByEmailVerificationToken(String token);

	@EntityGraph(attributePaths = "roles")
	Optional<UserEntity> findByPublicId(String publicId);

	@Query("SELECT CASE WHEN COUNT(following) > 0 THEN true ELSE false END "
			+ "FROM UserEntity follower JOIN follower.followings following WHERE follower.id = :followerId "
			+ "AND following.id = :followingId "
			)
	boolean isFollowing(@Param("followerId") long currentUserId, @Param("followingId") long targetUserId);
	
	@Modifying
	@Query(value = "DELETE FROM user_followings WHERE follower_id = :followerId AND following_id = :followingId",
			nativeQuery = true
			)
	void unfollowUser(@Param("followerId") long currentUserId, @Param("followingId") long targetUserId);
	
	//SELECT * FROM users JOIN user_followings AS user_f ON users.id=user_f.follower_id where user_f.following_id=:targetUserId 
	@Query("SELECT user FROM UserEntity user JOIN user.followings following "
			+ "WHERE following.id = :targetUserId")
	Page<UserEntity> findUserFollowers(@Param("targetUserId") long targetUserId, Pageable pageable);
	
	//SELECT * FROM users JOIN user_followings AS user_f ON users.id=user_f.following_id where user_f.follower_id=:targetUserId
	@Query("SELECT user FROM UserEntity user JOIN user.followers follower "
			+ "WHERE follower.id = :targetUserId")
	Page<UserEntity> findUserFollowings(@Param("targetUserId") long targetUserId, Pageable pageable);
}
