package com.crimson_code_blog_rest_apis.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	float id;

	@Column(name = "public_id", unique = true, nullable = false, length = 60)
	String publicId;

	@Column(name = "email", unique = true, nullable = false, length = 150)
	String email;

	@Column(name = "password", nullable = false, length = 100)
	String password;

	@Column(name = "first_name", nullable = false, length = 50)
	String firstName;

	@Column(name = "last_name", length = 50)
	String lastName;

	@Column(name = "joined_at", columnDefinition = "datetime default CURRENT_TIMESTAMP")
	private LocalDateTime joinedAt;

	public UserEntity() {

	}

	public UserEntity(String publicId, String email, String password, String firstName, String lastName,
			LocalDateTime joinedAt) {
		super();
		this.publicId = publicId;
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.joinedAt = joinedAt;
	}

	public float getId() {
		return id;
	}

	public void setId(float id) {
		this.id = id;
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public LocalDateTime getJoinedAt() {
		return joinedAt;
	}

	public void setJoinedAt(LocalDateTime joinedAt) {
		this.joinedAt = joinedAt;
	}

}
