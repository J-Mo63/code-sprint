package com.sudo_code.codesprint.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A Challenge refers to a specific a challenge in the database.
 * It stores the content of the question and the answer(s).
 */
public class Challenge implements Parcelable {

    // Class fields
    private int id;
    private String content;
    private String question;
    private String answer;

    /**
     * A constructor for a Challenge
     *
     * @param id - The challenge ID
     * @param content - The question content
     * @param question - The question text
     * @param answer - The accepted answer to the question
     */
    public Challenge(int id, String content, String question, String answer) {
        this.id = id;
        this.content = content;
        this.question = question;
        this.answer = answer;
    }

    /**
     * Includes parcel creation methods as a constant
     */
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
     * ID getter
     *
     * @return int - the challenge database ID
     */
    public int getId() {
        return id;
    }

    /**
     * Content getter
     *
     * @return String - the question content
     */
    public String getContent() {
        return content;
    }

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
        id = in.readInt();
        content = in.readString();
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
        dest.writeInt(id);
        dest.writeString(content);
        dest.writeString(question);
        dest.writeString(answer);
    }
}