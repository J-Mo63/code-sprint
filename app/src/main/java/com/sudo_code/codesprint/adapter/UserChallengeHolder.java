package com.sudo_code.codesprint.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.model.UserChallenge;

import java.util.Locale;

/**
 * A class for viewholder... for the adapter.
 */
public class UserChallengeHolder extends RecyclerView.ViewHolder {

    // UI fields
    private TextView usernameTextView;
    private TextView gradeTextView;
    private TextView timeTextView;
    private TextView rankTextView;

    /**
     * a constructor that has an onclick listener
     *
     * @param itemView - The current ViewHolder
     */
    public UserChallengeHolder(final View itemView) {
        super(itemView);
        usernameTextView = (TextView) itemView.findViewById(R.id.user_challenge_item_username);
        gradeTextView = (TextView) itemView.findViewById(R.id.user_challenge_item_grade);
        timeTextView = (TextView) itemView.findViewById(R.id.user_challenge_item_time);
        rankTextView = (TextView) itemView.findViewById(R.id.user_challenge_item_rank);
    }

    /**
     * Sets up the holder with the userChallenge elements it must model.
     *
     * @param userChallenge - the given userChallenge
     */
    public void setComponents(final UserChallenge userChallenge) {
        usernameTextView.setText(userChallenge.getUsername());
        gradeTextView.setText(userChallenge.getGrade());
        timeTextView.setText(formatTime(userChallenge.getTime()));
    }

    /**
     * A helper method to format longs into strings with the appropriate formatting.
     *
     * @param time - the long to be formatted
     * @return String - the formatted string
     */
    private String formatTime(long time) {
        double d = ((double) time)/1000;
        return String.format(Locale.UK, "%.2f", d) + "s";
    }
}
