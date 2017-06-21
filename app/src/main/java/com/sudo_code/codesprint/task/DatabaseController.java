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

public class DatabaseController {

    // Object fields
    private Activity mActivity;

    // Authentication fields
    private FirebaseUser currentUser;

    // Firebase fields
    private Firebase mFirebaseUser;

    // Database references
    private DatabaseReference mDatabaseUsers;

    // Constants
    private static final String DB_LINK_ADDRESS = "https://codesprint-e5f09.firebaseio.com/";
    private static final String USER_DB_REF = "User";
    private static final String USER_FOLLOW_DB_REF = "userFollows";
    private static final String USERNAME_FIELD_NAME = "username";
    private static final String USER_ID_FIELD_NAME = "id";


    public DatabaseController(Activity activity){
        mActivity = activity;

        // Set up authentication components
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
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


    public void deleteUserFollow(final String uId) {
        final String currentUid = currentUser.getUid();
        mFirebaseUser.child(currentUid).child(USER_FOLLOW_DB_REF).child(uId).removeValue();
    }

    public void createUser(String id, String username) {
        mFirebaseUser.child(id).setValue(new User(id, username));
    }

    private void addUserFollow(final String uId, final String username) {
        final String currentUid = currentUser.getUid();
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
                    newEntry.child(uId).setValue(true);
                    String msgText;
                    if (currentUid.equals(uId)) {
                        msgText = mActivity.getString(R.string.following_yourself_meme);
                    }
                    else {
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
