package com.sudo_code.codesprint.model;


public class UserFollow {
    private String userId;
    private String followedUserId;

    public UserFollow(){}

    public UserFollow(String userId, String followedUserId) {
        this.userId = userId;
        this.followedUserId = followedUserId;
    }

    public String getUserId() {
        return userId;
    }

    public String getFollowedUserId() {
        return followedUserId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFollowedUserId(String followedUserId) {
        this.followedUserId = followedUserId;
    }
}
