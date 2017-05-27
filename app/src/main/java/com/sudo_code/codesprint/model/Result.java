package com.sudo_code.codesprint.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A Result refers to a specific the result of a single completed Challenge.
 * It stores the ID of the Challenge and the time taken by the user.
 */
public class Result implements Parcelable {

    // Class fields
    private int challengeId;
    private long time;

    /**
     * A constructor for a Result
     *
     * @param challengeId - The id of the challenge
     * @param time - The accepted answer to the question
     */
    public Result(int challengeId, long time) {
        this.challengeId = challengeId;
        this.time = time;
    }

    /**
     * A Parcelable implementation constructor
     * @param in - the parcel being read to
     */
    protected Result(Parcel in) {
        challengeId = in.readInt();
        time = in.readLong();
    }

    /**
     * Includes parcel creation methods as a constant
     */
    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    /**
     * ID getter
     *
     * @return int - the challenge ID
     */
    public int getChallengeId() {
        return challengeId;
    }

    /**
     * Time getter
     *
     * @return Long - the time taken to answer
     */
    public long getTime() {
        return time;
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
        dest.writeInt(challengeId);
        dest.writeLong(time);
    }
}
