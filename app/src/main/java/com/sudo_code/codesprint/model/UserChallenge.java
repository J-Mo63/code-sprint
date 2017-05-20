package com.sudo_code.codesprint.model;

import java.util.Date;

/**
 * A UserChallenge refers to a specific instance of a completed challenge.
 * It stores the date of the challenge and the user results.
 */
public class UserChallenge {

    // Class fields
    private Date date;
    private String grade;
    private double time;

    /**
     * A constructor for a UserChallenge
     *
     * @param date - The date it was set/completed
     * @param grade - The grade achieved by the user
     * @param time - The total time taken to complete it
     */
    public UserChallenge(Date date, String grade, double time) {
        this.date = date;
        this.grade = grade;
        this.time = time;
    }

    /**
     * Date getter
     *
     * @return Date - the date
     */
    public Date getDate() {
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
    public double getTime() {
        return time;
    }
}
