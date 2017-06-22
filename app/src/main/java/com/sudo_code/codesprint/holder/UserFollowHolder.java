package com.sudo_code.codesprint.holder;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.helper.DatabaseController;

/**
 * A class for a viewholder used by the FirebaseRecyclerAdapter.
 */
public class UserFollowHolder extends RecyclerView.ViewHolder {

    // UI fields
    private TextView mUsernameTextView;
    private ImageView mUnfollowImg;

    // Object fields
    private DatabaseController mDatabaseController;
    private String mUserId;
    private String mUsername;

    /**
     * A constructor that sets up the views.
     *
     * @param itemView - The current ViewHolder
     */
    public UserFollowHolder(final View itemView) {
        super(itemView);
        // Set up a database controller
        mDatabaseController = new DatabaseController((Activity) itemView.getContext());
        ImageView rightIcon = (ImageView) itemView.findViewById(R.id.challenge_item_right_image_view);
        if (rightIcon != null) {
            rightIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }

        // Set up the views
        mUsernameTextView = (TextView) itemView.findViewById(R.id.user_item_username);
        mUnfollowImg = (ImageView) itemView.findViewById(R.id.user_item_delete);
    }

    /**
     * Displays the username in the view.
     *
     * @param username - the username to display
     */
    public void setUsername(String username) {
        mUsernameTextView.setText(username);
    }

    /**
     * Sets the id of the user and sets up the confirmation dialogue for the delete.
     *
     * @param uId - the user's id
     * @param username - the user's username
     */
    public void setDelete(final String uId, final String username) {
        mUserId = uId;
        mUsername = username;
        mUnfollowImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setMessage(itemView.getContext().getString(R.string.unfollow_conf_dialogue)
                        + username 
                        + itemView.getContext().getString(R.string.question_mark))
                        .setPositiveButton(R.string.yes, dialogClickListener)
                        .setNegativeButton(R.string.no, dialogClickListener).show();
            }
        });
    }

    /**
     * An onClick listener to handle the confirmation dialogue.
     */
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    mDatabaseController.deleteUserFollow(mUserId);
                    Toast.makeText(itemView.getContext(),
                            itemView.getContext().getString(R.string.unfollowed_notification_text)
                                    + mUsername,
                            Toast.LENGTH_SHORT).show();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };
}
