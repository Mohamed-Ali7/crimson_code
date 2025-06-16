package com.crimson_code_blog_rest_apis.dto.response;

public class FollowingStatusResponseModel {

	boolean isFollowing;

	public FollowingStatusResponseModel() {
	}

	public FollowingStatusResponseModel(boolean isFollowing) {
		this.isFollowing = isFollowing;
	}

	public boolean isFollowing() {
		return isFollowing;
	}

	public void setFollowing(boolean isFollowing) {
		this.isFollowing = isFollowing;
	}
	
}
