package com.sudo_code.codesprint.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.task.UserLoginTask;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI fields
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;


    /**
     * Defines login form fields and sets up triggers
     *
     * @param savedInstanceState - the saved bundle state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Attempt to login using
        UserLoginTask autoLogin = new UserLoginTask(null, null, this);
        autoLogin.execute();

        // Set up the login form.
        mUsernameEditText = (EditText) findViewById(R.id.login_username_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.login_password_edit_text);

        // On submitting login form through button
        Button mUsernameSignInButton = (Button) findViewById(R.id.login_sign_in_button);
        mUsernameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        // On submitting login form through 'enter' key
        mPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
    }


    /**
     * Attempts to sign in.
     * If there are form errors (missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mUsernameEditText.setError(null);
        mPasswordEditText.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        // Handle unfilled forms
        boolean cancel = (username.equals("") || password.equals(""));
        View focusView = null;
        String fieldName = "";

        if (username.equals("")) {
            focusView = mUsernameEditText;
            fieldName = getString(R.string.prompt_username).toLowerCase();
            cancel = true;
        }
        else if (password.equals("")) {
            focusView = mPasswordEditText;
            fieldName = getString(R.string.prompt_password).toLowerCase();
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null) {
                focusView.requestFocus();
            }
            Toast.makeText(this,
                    "Please enter a " + fieldName,
                    Toast.LENGTH_SHORT).show();
        }
        else {
            // Kick off a background task to perform the user login attempt.
            UserLoginTask authTask = new UserLoginTask(username, password, LoginActivity.this);
            authTask.execute();
        }
    }
}

