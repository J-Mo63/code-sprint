package com.sudo_code.codesprint.model;

import java.util.Date;

public class Challenge {

    private Date date;
    private String grade;
    private double time;

    public Challenge(Date date, String grade, double time) {
        this.date = date;
        this.grade = grade;
        this.time = time;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }
}
