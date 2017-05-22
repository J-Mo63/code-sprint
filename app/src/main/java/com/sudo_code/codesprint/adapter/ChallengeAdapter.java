package com.sudo_code.codesprint.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.model.UserChallenge;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An adapter class for the recycler to hold UserChallenge items.
 */
public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ViewHolder> {

    // Object fields
    private List<UserChallenge> userChallenges;


    /**
     * A constructor for the adapter class.
     *
     * @param userChallenges - A list of items for the recycler
     */
    public ChallengeAdapter(ArrayList<UserChallenge> userChallenges) {
        this.userChallenges = userChallenges;
    }


    /**
     * Things to do on the creation of the viewholder.
     *
     * @param parent - Parent view
     * @param viewType - Type of view
     * @return ViewHolder - Inflated recycler item
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View challengeItem = inflater.inflate(R.layout.challenge_item, parent, false);

        return new ViewHolder(challengeItem);
    }


    /**
     * Things to do when binding the viewholder.
     *
     * @param holder - Reference to holder
     * @param position - Item postion
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final UserChallenge userChallenge = userChallenges.get(position);

        holder.dayTextView.setText("Monday");
        holder.gradeTextView.setText(userChallenge.getGrade());
        holder.timeTextView.setText(String.format(Locale.UK, "%1$,.2f", userChallenge.getTime()));
    }


    /**
     * Returns the current size of the adapter items.
     *
     * @return int - Number of items
     */
    @Override
    public int getItemCount() {
        return userChallenges.size();
    }


    /**
     * A class for viewholder... for the adapter.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        // UI fields
        private TextView dayTextView;
        private TextView gradeTextView;
        private TextView timeTextView;

        /**
         * a constructor that has an onclick listener
         *
         * @param itemView - The current ViewHolder
         */
        private ViewHolder(final View itemView) {
            super(itemView);
            ImageView rightIcon = (ImageView) itemView.findViewById(R.id.challenge_item_right_image_view);
            if (rightIcon != null) {
                rightIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }
            dayTextView = (TextView) itemView.findViewById(R.id.challenge_item_day);
            gradeTextView = (TextView) itemView.findViewById(R.id.challenge_item_grade);
            timeTextView = (TextView) itemView.findViewById(R.id.challenge_item_time);
        }
    }
}
