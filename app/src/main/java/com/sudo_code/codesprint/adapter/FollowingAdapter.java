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
import android.widget.Toast;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.model.User;
import java.util.ArrayList;
import java.util.List;

/**
 * An adapter class for the recycler to hold User items.
 */
public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {

    // Object fields
    private List<User> mUsers;


    /**
     * A constructor for the adapter class.
     *
     * @param users - A list of items for the recycler
     */
    public FollowingAdapter(ArrayList<User> users) {
        this.mUsers = users;
    }


    /**
     * Things to do on the creation of the viewholder.
     *
     * @param parent - Parent view
     * @param viewType - Type of view
     * @return ViewHolder - Inflated recycler item
     */
    @Override
    public FollowingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View userItem = inflater.inflate(R.layout.user_item, parent, false);

        return new FollowingAdapter.ViewHolder(userItem);
    }


    /**
     * Things to do when binding the viewholder.
     *
     * @param holder - Reference to holder
     * @param position - Item postion
     */
    @Override
    public void onBindViewHolder(final FollowingAdapter.ViewHolder holder, int position) {
        final User user = mUsers.get(position);

        holder.mUsernameTextView.setText(user.getUsername());

        holder.mUnfollowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),
                        view.getContext().getString(R.string.unfollowed_notification_text)
                                + user.getUsername(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Returns the current size of the adapter items.
     *
     * @return int - Number of items
     */
    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    /**
     * A class for viewholder... for the adapter.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        // UI fields
        private TextView mUsernameTextView;
        private ImageView mUnfollowImg;

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
            mUsernameTextView = (TextView) itemView.findViewById(R.id.user_item_username);
            mUnfollowImg = (ImageView) itemView.findViewById(R.id.user_item_delete);
        }
    }

}