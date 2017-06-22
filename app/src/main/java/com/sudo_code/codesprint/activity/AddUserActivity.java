package com.sudo_code.codesprint.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.task.DatabaseController;

/**
 * A signup screen that allows the user to signup with a username, email and password.
 */
public class AddUserActivity extends AppCompatActivity {

    // UI fields
    private TextView mDescriptionTextView;
    private LinearLayout mForm;
    private ProgressBar mProgress;
    private EditText mUsernameEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    // Object fields
    private DatabaseController mDbController;

    // Authentication objects
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser mCurrentUser;

    /**
     * Defines signup form fields and sets up triggers
     *
     * @param savedInstanceState - the saved bundle state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appbar_add_user_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.sign_up_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up the login form.
        mDescriptionTextView = (TextView) findViewById(R.id.add_user_description_textview);
        mForm = (LinearLayout) findViewById(R.id.add_user_form);
        mProgress = (ProgressBar) findViewById(R.id.add_user_progress);
        mUsernameEditText = (EditText) findViewById(R.id.add_user_username_edit_text);
        mEmailEditText = (EditText) findViewById(R.id.add_user_email_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.add_user_password_edit_text);

        Button signUnButton = (Button) findViewById(R.id.add_user_sign_up_button);
        signUnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForm(false);
                attemptSignup();
            }
        });

        // Set up authentication components
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mCurrentUser = firebaseAuth.getCurrentUser();
            }
        };

        // Create a new DB Controller instance
        mDbController = new DatabaseController(this);
    }

    /**
     * Is called when an option is selected by the user.
     *
     * @param item - The options item selected
     * @return boolean - success
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home ) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * On starting the activity, connect the AuthStateListener.
     */
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Attempts to sign up.
     * If there are form errors (missing fields, etc.), the
     * errors are presented and no actual signup attempt is made.
     */
    private void attemptSignup() {
        // Store values at the time of the signup attempt.
        final String username = mUsernameEditText.getText().toString();
        final String email = mEmailEditText.getText().toString();
        final String password = mPasswordEditText.getText().toString();

        if (formHasErrors(username, email, password)) {
            focusErrors(username, email, password);
            showForm(true);
        }
        else {
            // Kick off a background task to perform the user signup attempt
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Add user displayname
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username).build();
                                mCurrentUser.updateProfile(profileUpdates);

                                // Add the user to the database
                                mDbController.createUser(mAuth.getCurrentUser().getUid(), username);

                                // Log the user in
                                showForm(false);
                                Toast.makeText(getApplicationContext(), R.string.account_created_notification,
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                // Display login form again
                                showForm(true);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String feedback = getString(R.string.signup_error_generic);
                            if (e instanceof FirebaseAuthUserCollisionException) {
                                feedback = getString(R.string.signup_error_email_collision);
                            }
                            else if (e instanceof FirebaseAuthWeakPasswordException) {
                                feedback = getString(R.string.signup_error_simple_password);
                            }
                            Toast.makeText(getApplicationContext(), feedback,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /**
     * A helper method to check if the credentials provided are valid.
     *
     * @param username - the input username
     * @param email - the input email
     * @param password - the input password
     * @return boolean - whether the form has errors or not
     */
    private boolean formHasErrors(String username, String email, String password) {
        return (username.equals("") || email.equals("") || password.equals(""));
    }


    /**
     * Shows and hides the login form / progress bar.
     *
     * @param show - whether to show or not
     */
    private void showForm(boolean show) {
        int visibilityOne = (show ? View.GONE : View.VISIBLE);
        int visibilityTwo = (show ? View.VISIBLE : View.GONE);

        mDescriptionTextView.setVisibility(visibilityTwo);
        mForm.setVisibility(visibilityTwo);
        mProgress.setVisibility(visibilityOne);
    }


    /**
     * Find (if any) fields that are blank and focuses on them.
     *
     * @param username - the input username
     * @param email - the input email
     * @param password - the input password
     */
    private void focusErrors(String username, String email, String password) {
        // Reset errors.
        mUsernameEditText.setError(null);
        mEmailEditText.setError(null);
        mPasswordEditText.setError(null);

        // Find the first problem
        View focusView = null;
        String fieldName = "";

        if (username.equals("")) {
            focusView = mUsernameEditText;
            fieldName = getString(R.string.prompt_username).toLowerCase();
        }
        else if (email.equals("")) {
            focusView = mEmailEditText;
            fieldName = getString(R.string.prompt_email).toLowerCase();
        }
        else if (password.equals("")) {
            focusView = mPasswordEditText;
            fieldName = getString(R.string.prompt_password).toLowerCase();
        }

        // Focus the first form field with an error
        if (focusView != null) {
            focusView.requestFocus();
        }
        Toast.makeText(this, getString(R.string.form_error) + fieldName, Toast.LENGTH_SHORT).show();
    }

}
