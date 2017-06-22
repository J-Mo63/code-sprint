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
import com.google.firebase.auth.FirebaseUser;
import com.sudo_code.codesprint.R;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {

    // Constants
    private static final String EMAIL = "email_tag";
    public static final String USERNAME = "username_tag";
    private static final String PASSWORD = "password_tag";
    public static final String USER_ID = "user_id_tag";

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
    private FirebaseUser currentUser;


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
                Intent signupIntent = new Intent(getApplicationContext(), AddUserActivity.class);
                startActivity(signupIntent);
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
                currentUser = firebaseAuth.getCurrentUser();
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
        String username = mSharedPrefs.getString(EMAIL, null);
        String password = mSharedPrefs.getString(PASSWORD, null);

        // If they exist, attempt a login
        if (username != null) {
            showForm(false);
            attemptLogin(username, password);
        }
    }


    /**
     * Attempts to sign in.
     * If there are form errors (missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(final String email, final String password) {

        if (formHasErrors(email, password)) {
            focusErrors(email, password);
            showForm(true);
        }
        else {
            // Kick off a background task to perform the user login attempt
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this,
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Save login details
                                mSharedPrefs.edit().putString(EMAIL, email).apply();
                                mSharedPrefs.edit().putString(PASSWORD, password).apply();
                                mSharedPrefs.edit().putString(USER_ID, currentUser.getUid()).apply();
                                mSharedPrefs.edit().putString(USERNAME, currentUser.getDisplayName()).apply();

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
                            SharedPreferences prefs = PreferenceManager
                                    .getDefaultSharedPreferences(getApplicationContext());
                            prefs.edit().clear().apply();
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

