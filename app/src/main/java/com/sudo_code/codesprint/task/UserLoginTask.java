package com.sudo_code.codesprint.task;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.activity.HomeActivity;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    // Constants
    private static final String USERNAME = "username_tag";
    private static final String PASSWORD = "password_tag";

    // Object fields
    private Activity mLoginActivity;
    private String mUsername;
    private String mPassword;
    private SharedPreferences mSharedPrefs;
    private boolean mIsAuto;


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
        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mLoginActivity);
        mIsAuto = false;

        // Fetch stored details if any
        if (mUsername == null && mPassword == null){
            mIsAuto = true;
            mUsername = mSharedPrefs.getString(USERNAME, null);
            mPassword = mSharedPrefs.getString(PASSWORD, null);
        }
    }

    /**
     * Performs operations before backround task begins like hiding the forms or cancelling
     * the task.
     */
    @Override
    protected void onPreExecute() {
        // Hide the login form
        showLoginForm(false);

        // Cancel the task if there are no stored details
        if (mUsername == null) {
            this.cancel(true);
            showLoginForm(true);
        }
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
            Thread.sleep(2000);
        } catch (InterruptedException e) {
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
            // Save login details
            mSharedPrefs.edit().putString(USERNAME, mUsername).apply();
            mSharedPrefs.edit().putString(PASSWORD, mPassword).apply();

            // Open to the home activity
            Intent intent = new Intent(mLoginActivity, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);

            if (!mIsAuto) {
                Toast.makeText(mLoginActivity.getApplicationContext(),
                        R.string.signed_in_notification,
                        Toast.LENGTH_SHORT).show();
            }
            mLoginActivity.startActivity(intent);
            mLoginActivity.finish();
        }
        else {
            // Display login form again
            Toast.makeText(mLoginActivity.getApplicationContext(),
                    R.string.error_incorrect_details,
                    Toast.LENGTH_SHORT).show();
            showLoginForm(true);
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


    /**
     *  Shows and hides the login form / progress bar.
     *
     * @param show - whether to show or not
     */
    private void showLoginForm(boolean show) {
        int visibilityOne = (show ? View.GONE : View.VISIBLE);
        int visibilityTwo = (show ? View.VISIBLE : View.GONE);

        mLoginActivity.findViewById(R.id.login_description_textview).setVisibility(visibilityTwo);
        mLoginActivity.findViewById(R.id.login_form).setVisibility(visibilityTwo);
        mLoginActivity.findViewById(R.id.login_progress).setVisibility(visibilityOne);
    }
}
