package com.sudo_code.codesprint.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A Challenge refers to a specific a challenge in the database.
 * It stores the content of the question and the answer(s).
 */
public class Challenge implements Parcelable {

    // Class fields
    private String question;
    private String answer;

    /**
     * A constructor for a Challenge
     *
     * @param question - The question text
     * @param answer - The accepted answer to the question
     */
    public Challenge(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public static final Creator<Challenge> CREATOR = new Creator<Challenge>() {
        @Override
        public Challenge createFromParcel(Parcel in) {
            return new Challenge(in);
        }

        @Override
        public Challenge[] newArray(int size) {
            return new Challenge[size];
        }
    };

    /**
     * Question getter
     *
     * @return String - the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Answer getter
     *
     * @return String - the answer to the question
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * A Parcelable implementation constructor
     * @param in - the parcel being read to
     */
    protected Challenge(Parcel in) {
        question = in.readString();
        answer = in.readString();
    }

    /**
     * @return int - descibes the contents of the Parcelable implemenation
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flattens the object into a parcel
     *
     * @param dest - destination Parcel
     * @param flags - additional options
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeString(answer);
    }
}