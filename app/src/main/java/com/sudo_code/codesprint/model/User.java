package com.sudo_code.codesprint.model;

/**
 * A User refers to a user's details and contains data about them including thier
 * username and id.
 */
public class User {

    // Class fields
    private String id;
    private String username;

    public User(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}
