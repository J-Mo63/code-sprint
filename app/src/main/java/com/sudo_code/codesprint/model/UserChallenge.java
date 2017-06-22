package com.sudo_code.codesprint.model;

/**
 * A UserChallenge refers to a specific instance of a completed challenge.
 * It stores the date of the challenge and the user results.
 */
public class UserChallenge {

    // Class fields
    private String username;
    private String date;
    private String grade;
    private long time;

    public UserChallenge() {}

    /**
     * A constructor for a UserChallenge
     *
     * @param date - The date it was set/completed
     * @param grade - The grade achieved by the user
     * @param time - The total time taken to complete it
     */
    public UserChallenge(String username, String date, String grade, long time) {
        this.username = username;
        this.date = date;
        this.grade = grade;
        this.time = time;
    }

    /**
     * Date getter
     *
     * @return Date - the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Grade getter
     *
     * @return String - a grade
     */
    public String getGrade() {
        return grade;
    }

    /**
     * Time getter
     *
     * @return double - the time
     */
    public long getTime() {
        return time;
    }

    public String getUsername() {
        return username;
    }
}
