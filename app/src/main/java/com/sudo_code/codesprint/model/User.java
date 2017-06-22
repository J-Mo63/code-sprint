package com.sudo_code.codesprint.model;

/**
 * A User refers to a user's details and contains data about them including thier
 * username and id.
 */
public class User {

    // Class fields
    private String id;
    private String username;

    /**
     * An empty constructer used by Firebase to populate objects.
     */
    public User() {}

    /**
     * A constructor for a User
     *
     * @param id - the user's id
     * @param username - the user's username
     */
    public User(String id, String username) {
        this.id = id;
        this.username = username;
    }

    /**
     * An id getter method.
     *
     * @return String - id
     */
    public String getId() {
        return id;
    }

    /**
     * A username getter method.
     *
     * @return String - username
     */
    public String getUsername() {
        return username;
    }

    /**
     * A setter for the id field.
     *
     * @param id - the new id
     */
    public void setId(String id) {
        this.id = id;
    }
}
