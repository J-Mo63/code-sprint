package com.sudo_code.codesprint.task;

import android.app.Activity;
import android.support.annotation.NonNull;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sudo_code.codesprint.model.User;
import com.sudo_code.codesprint.model.UserFollow;

public class DatabaseController {

    // Authentication fields
    private FirebaseUser currentUser;

    // Firebase fields
    private Firebase mFirebaseUser;
    private Firebase mFirebaseUserFollow;

    public DatabaseController(Activity activity){


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
        mFirebaseUser = new Firebase("https://codesprint-e5f09.firebaseio.com/User");
        mFirebaseUserFollow = new Firebase("https://codesprint-e5f09.firebaseio.com/UserFollow");


        // Set up authStateListener
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void createUserFollow(String enteredUsername) {
        String currentUid = currentUser.getUid();

        Firebase newEntry = mFirebaseUserFollow.push();
        newEntry.setValue(new UserFollow(currentUid, enteredUsername));
    }

    public void createUser(String id, String email) {
        Firebase newEntry = mFirebaseUser.push();
        newEntry.setValue(new User(id, email));
    }
}
