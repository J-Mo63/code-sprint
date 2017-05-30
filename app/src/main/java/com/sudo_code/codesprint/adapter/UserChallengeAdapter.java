package com.sudo_code.codesprint.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.model.UserChallenge;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * An adapter class for the recycler to hold UserChallenge items.
 */
public class UserChallengeAdapter extends RecyclerView.Adapter<UserChallengeAdapter.ViewHolder> {

    // Object fields
    private List<UserChallenge> mUserChallenges;


    /**
     * A constructor for the adapter class.
     *
     * @param userChallenges - A list of items for the recycler
     */
    public UserChallengeAdapter(ArrayList<UserChallenge> userChallenges) {
        this.mUserChallenges = userChallenges;
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

        View challengeItem = inflater.inflate(R.layout.user_challenge_item, parent, false);

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
        final UserChallenge userChallenge = mUserChallenges.get(position);

        holder.usernameTextView.setText(userChallenge.getUsername());
        holder.gradeTextView.setText(userChallenge.getGrade());
        holder.rankTextView.setText(humaniseRank(position + 1));
        holder.timeTextView.setText(String.format(Locale.UK, "%1$,.2f", userChallenge.getTime()));
    }


    /**
     * Returns the current size of the adapter items.
     *
     * @return int - Number of items
     */
    @Override
    public int getItemCount() {
        return mUserChallenges.size();
    }


    /**
     * A class for viewholder... for the adapter.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

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
        private ViewHolder(final View itemView) {
            super(itemView);
            usernameTextView = (TextView) itemView.findViewById(R.id.user_challenge_item_username);
            gradeTextView = (TextView) itemView.findViewById(R.id.user_challenge_item_grade);
            timeTextView = (TextView) itemView.findViewById(R.id.user_challenge_item_time);
            rankTextView = (TextView) itemView.findViewById(R.id.user_challenge_item_rank);
        }
    }

    /**
     * A helper method to return a number formatted as a rank.
     *
     * @param number - the number to be formatted.
     * @return String - the formatted rank.
     */
    private String humaniseRank(int number) {
        if (number == 1) {
            return number + "st";
        }
        else if (number == 2) {
            return number + "nd";
        }
        else if (number == 3) {
            return number + "rd";
        }
        return number + "th";
    }
}
