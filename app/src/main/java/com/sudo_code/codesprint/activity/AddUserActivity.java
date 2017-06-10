package com.sudo_code.codesprint.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.task.UserSignupTask;

/**
 * A signup screen that allows the user to signup with a username and password.
 */
public class AddUserActivity extends AppCompatActivity {

    // UI fields
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;

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
        mUsernameEditText = (EditText) findViewById(R.id.add_user_username_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.add_user_password_edit_text);

        Button signUnButton = (Button) findViewById(R.id.add_user_sign_up_button);
        signUnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
            }
        });
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
     * Attempts to sign up.
     * If there are form errors (missing fields, etc.), the
     * errors are presented and no actual signup attempt is made.
     */
    private void attemptSignup() {

        // Reset errors.
        mUsernameEditText.setError(null);
        mPasswordEditText.setError(null);

        // Store values at the time of the signup attempt.
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
            // There was an error; don't attempt signup and focus the first
            // form field with an error.
            if (focusView != null) {
                focusView.requestFocus();
            }
            Toast.makeText(this,
                    "Please enter a " + fieldName,
                    Toast.LENGTH_SHORT).show();
        }
        else {
            // Kick off a background task to perform the user signup attempt
            UserSignupTask signupTask = new UserSignupTask(username, password, AddUserActivity.this);
            signupTask.execute();
        }
    }

}
