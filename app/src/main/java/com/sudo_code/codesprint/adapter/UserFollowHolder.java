package com.sudo_code.codesprint.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.task.DatabaseController;

/**
 * A class for viewholder... for the adapter.
 */
public class UserFollowHolder extends RecyclerView.ViewHolder {

    // UI fields
    private TextView mUsernameTextView;
    private ImageView mUnfollowImg;

    private DatabaseController mDatabaseController;

    /**
     * a constructor that has an onclick listener
     *
     * @param itemView - The current ViewHolder
     */
    public UserFollowHolder(final View itemView) {
        super(itemView);
        mDatabaseController = new DatabaseController((Activity) itemView.getContext());
        ImageView rightIcon = (ImageView) itemView.findViewById(R.id.challenge_item_right_image_view);
        if (rightIcon != null) {
            rightIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        mUsernameTextView = (TextView) itemView.findViewById(R.id.user_item_username);
        mUnfollowImg = (ImageView) itemView.findViewById(R.id.user_item_delete);
    }

    public void setUsername(String username) {
        mUsernameTextView.setText(username);
    }

    public void setDelete(final String uId) {
        mUnfollowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseController.deleteUserFollow(uId);
            }
        });
    }
}
