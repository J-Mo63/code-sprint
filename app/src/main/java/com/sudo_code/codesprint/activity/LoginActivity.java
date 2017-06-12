package com.sudo_code.codesprint.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.sudo_code.codesprint.R;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {

    // Constants
    private static final String USERNAME = "username_tag";
    private static final String PASSWORD = "password_tag";

    // UI fields
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private TextView mDescriptionTextView;
    private LinearLayout mForm;
    private ProgressBar mProgress;

    // Object fields
    private SharedPreferences mSharedPrefs;

    // Authentication objects
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    /**
     * Defines login form fields, sets up triggers and initialises authentication.
     * Will attempt an autoLogin once.
     *
     * @param savedInstanceState - the saved bundle state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get a reference to shared preferences
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Set up UI components
        mUsernameEditText = (EditText) findViewById(R.id.login_username_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.login_password_edit_text);
        mDescriptionTextView = (TextView) findViewById(R.id.login_description_textview);
        mProgress = (ProgressBar) findViewById(R.id.login_progress);
        mForm = (LinearLayout) findViewById(R.id.login_form);

        // On submitting form through button
        Button usernameSignInButton = (Button) findViewById(R.id.login_sign_in_button);
        Button usernameSignUpButton = (Button) findViewById(R.id.login_sign_up_button);

        usernameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showForm(false);
                // Store values at the time of the login attempt.
                String username = mUsernameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                attemptLogin(username, password);
            }
        });

        usernameSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showForm(false);
                attemptSignup();
            }
        });

        // On submitting login form through 'enter' key
        mPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    showForm(false);
                    // Store values at the time of the login attempt.
                    String username = mUsernameEditText.getText().toString();
                    String password = mPasswordEditText.getText().toString();
                    attemptLogin(username, password);
                    return true;
                }
                return false;
            }
        });

        // Set up authentication components
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };

        // Automatically attempt to login using stored details
        attemptAutoLogin();
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
     * On stopping the activity, disconnect the AuthStateListener.
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    /**
     * Attempts to discretely log the user in using stored details
     */
    private void attemptAutoLogin() {

        // Fetch previously stored details
        String username = mSharedPrefs.getString(USERNAME, null);
        String password = mSharedPrefs.getString(PASSWORD, null);

        // If they exist, attempt a login
        if (username != null) {
            showForm(false);
            attemptLogin(username, password);
        }
    }


    /**
     * Attempts to sign up.
     * If there are form errors (missing fields, etc.), the
     * errors are presented and no actual signup attempt is made.
     */
    private void attemptSignup() {
        // Store values at the time of the signup attempt.
        final String username = mUsernameEditText.getText().toString();
        final String password = mPasswordEditText.getText().toString();

        if (formHasErrors(username, password)) {
            focusErrors(username, password);
            showForm(true);
        }
        else {
            // Kick off a background task to perform the user signup attempt
            mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Log the user in
                                showForm(false);
                                attemptLogin(username, password);
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
     * Attempts to sign in.
     * If there are form errors (missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(final String username, final String password) {

        if (formHasErrors(username, password)) {
            focusErrors(username, password);
            showForm(true);
        }
        else {
            // Kick off a background task to perform the user login attempt
            mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Save login details
                                mSharedPrefs.edit().putString(USERNAME, username).apply();
                                mSharedPrefs.edit().putString(PASSWORD, password).apply();

                                // Open to the home activity
                                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.signed_in_notification),
                                        Toast.LENGTH_SHORT).show();
                                startActivity(intent);
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
                            String feedback = getString(R.string.signin_error_generic);
                            if (e instanceof FirebaseAuthInvalidUserException) {
                                feedback = getString(R.string.signin_error_no_user);
                            }
                            else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                feedback = getString(R.string.signin_error_bad_password);
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
     * @param password - the input password
     * @return boolean - whether the form has errors or not
     */
    private boolean formHasErrors(String username, String password) {
        return (username.equals("") || password.equals(""));
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
     * @param password - the input password
     */
    private void focusErrors(String username, String password) {
        // Reset errors.
        mUsernameEditText.setError(null);
        mPasswordEditText.setError(null);

        // Find the first problem
        View focusView = null;
        String fieldName = "";

        if (username.equals("")) {
            focusView = mUsernameEditText;
            fieldName = getString(R.string.prompt_username).toLowerCase();
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

