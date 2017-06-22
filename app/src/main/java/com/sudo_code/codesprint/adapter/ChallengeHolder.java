package com.sudo_code.codesprint.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.activity.UserChallengeActivity;
import com.sudo_code.codesprint.model.UserChallenge;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A class for the ChallengeHolder used by the Firebase UI
 */
public class ChallengeHolder extends RecyclerView.ViewHolder {

    // UI fields
    private TextView dayTextView;
    private TextView gradeTextView;
    private TextView timeTextView;
    private LinearLayout itemLayout;

    /**
     * A constructor that has an onclick listener
     *
     * @param itemView - The current ViewHolder
     */
    public ChallengeHolder(final View itemView) {
        super(itemView);
        ImageView rightIcon = (ImageView) itemView.findViewById(R.id.challenge_item_right_image_view);
        if (rightIcon != null) {
            rightIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        dayTextView = (TextView) itemView.findViewById(R.id.challenge_item_day);
        gradeTextView = (TextView) itemView.findViewById(R.id.challenge_item_grade);
        timeTextView = (TextView) itemView.findViewById(R.id.challenge_item_time);
        itemLayout = (LinearLayout) itemView.findViewById(R.id.challenge_item_layout);
    }

    /**
     * Sets up the holder with the userChallenge elements it must model.
     *
     * @param userChallenge - the given userChallenge
     */
    public void setComponents(UserChallenge userChallenge) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String dateString;
        try {
            Date date = formatter.parse(userChallenge.getDate());
            formatter.applyPattern("EEEE d MMM yyyy");
            dateString = formatter.format(date);
        }
        catch (ParseException e) {
            dateString = userChallenge.getDate();
        }

        dayTextView.setText(dateString);
        gradeTextView.setText(userChallenge.getGrade());
        timeTextView.setText(formatTime(userChallenge.getTime()));
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent beginChallengeIntent = new Intent(view.getContext(), UserChallengeActivity.class);

                

                view.getContext().getApplicationContext().startActivity(beginChallengeIntent);
            }
        });
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
