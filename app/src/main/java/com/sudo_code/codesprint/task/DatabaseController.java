package com.sudo_code.codesprint.task;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.model.User;

/**
 *
 */
public class DatabaseController {

    // Object fields
    private Activity mActivity;

    // Authentication fields
    private FirebaseUser mCurrentUser;

    // Firebase fields
    private Firebase mFirebaseUser;

    // Database references
    private DatabaseReference mDatabaseUsers;

    // Constants
    private static final String DB_LINK_ADDRESS = "https://codesprint-e5f09.firebaseio.com/";
    public static final String USER_DB_REF = "User";
    public static final String USER_FOLLOW_DB_REF = "userFollows";
    private static final String USERNAME_FIELD_NAME = "username";
    private static final String USER_ID_FIELD_NAME = "id";

    /**
     * A constructor for the DatabaseController that sets up authentication,
     * defines Firebase paths and defines references.
     *
     * @param activity - the calling activity
     */
    public DatabaseController(Activity activity){
        mActivity = activity;

        // Set up authentication components
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mCurrentUser = firebaseAuth.getCurrentUser();
            }
        };

        // Firebase stuff
        Firebase.setAndroidContext(activity);

        // Define database paths
        mFirebaseUser = new Firebase(DB_LINK_ADDRESS + USER_DB_REF);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsers = database.child(USER_DB_REF);

        // Set up authStateListener
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Checks to see if a user exists in the database, and calls another method
     * to follow them in the case that they do.
     *
     * @param username - the input username
     */
    public void createUserFollow(final String username) {
        mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if (data.child(USERNAME_FIELD_NAME).getValue().equals(username)) {
                        addUserFollow((String) data.child(USER_ID_FIELD_NAME).getValue(), username);
                        found = true;
                    }
                }
                if (!found) {
                    Toast.makeText(mActivity, R.string.no_user_found_error,
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mActivity, R.string.error_occured_general,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Unfollows a user based on thier userid.
     *
     * @param uId - the userid of the user to be unfollowed
     */
    public void deleteUserFollow(final String uId) {
        final String currentUid = mCurrentUser.getUid();
        mFirebaseUser.child(currentUid).child(USER_FOLLOW_DB_REF).child(uId).removeValue();
    }

    /**
     * Creates a database entry for a user.
     *
     * @param id - the id of the user to be created
     * @param username - the username of the user to be created
     */
    public void createUser(String id, String username) {
        mFirebaseUser.child(id).setValue(new User(id, username));
    }

    /**
     * A helper method to check if one already follows a user. If they do not already
     * follow the, creates a userFollow under the user entry in the database.
     *
     * @param uId - the id of the user to follow
     * @param username - the username of the user to follow
     */
    private void addUserFollow(final String uId, final String username) {
        final String currentUid = mCurrentUser.getUid();
        final Firebase newEntry = mFirebaseUser.child(currentUid).child(USER_FOLLOW_DB_REF);

        DatabaseReference databaseUserFollows = mDatabaseUsers.child(currentUid).child(USER_FOLLOW_DB_REF);

        databaseUserFollows.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if (data.getKey().equals(uId)) {
                        Toast.makeText(mActivity,
                                mActivity.getString(R.string.already_following_error) + username,
                                Toast.LENGTH_SHORT).show();
                        found = true;
                    }
                }
                if (!found) {
                    String msgText;
                    if (currentUid.equals(uId)) {
                        msgText = mActivity.getString(R.string.following_yourself_meme);
                    }
                    else {
                        newEntry.child(uId).setValue(true);
                        msgText = mActivity.getString(R.string.followed_text) + username;
                    }
                    Toast.makeText(mActivity, msgText, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(mActivity, R.string.error_occured_general,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
