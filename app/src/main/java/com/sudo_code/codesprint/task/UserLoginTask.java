package com.sudo_code.codesprint.task;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.activity.HomeActivity;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    // Object fields
    private Activity mLoginActivity;
    private final String mUsername;
    private final String mPassword;


    /**
     * UserLoginTask constructor that gets input username and password as well as the calling
     * activity for reference.
     *
     * @param username - the entered username
     * @param password - the entered password
     * @param loginActivity - the calling activity
     */
    public UserLoginTask(String username, String password, Activity loginActivity) {
        mUsername = username;
        mPassword = password;
        mLoginActivity = loginActivity;
    }


    /**
     * Attempts to log the user in
     *
     * @param params - nothing at all
     * @return boolean - denoting whether login attempt successful
     */
    @Override
    protected Boolean doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.

        try {
            // Simulate network access.
            Thread.sleep(4000);
        }
        catch (InterruptedException e) {
            return false;
        }

        return isValid(mUsername, mPassword);
    }

    /**
     * Either returns to login form or opens homescreen depending on whether
     * the login attempt is successful.
     *
     * @param success - boolean denoting whether login attempt successful
     */
    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            // Open to the home activity
            Intent intent = new Intent(mLoginActivity, HomeActivity.class);
            Toast.makeText(mLoginActivity.getApplicationContext(),
                    R.string.signed_in_notification,
                    Toast.LENGTH_SHORT).show();
            mLoginActivity.startActivity(intent);
        }
        else {
            // Display login form again
            Toast.makeText(mLoginActivity.getApplicationContext(),
                    R.string.error_incorrect_details,
                    Toast.LENGTH_SHORT).show();
            mLoginActivity.findViewById(R.id.login_description_textview).setVisibility(View.VISIBLE);
            mLoginActivity.findViewById(R.id.login_progress).setVisibility(View.GONE);
            mLoginActivity.findViewById(R.id.login_form).setVisibility(View.VISIBLE);
        }
    }

    /**
     * Stub method to validate login details.
     *
     * @param username - the input username
     * @param password - the input password
     * @return boolean - the success status of the attempt
     */
    private boolean isValid(String username, String password) {
        return (username.equals("j-mo") && password.equals("rmsgnu"));
    }
}
