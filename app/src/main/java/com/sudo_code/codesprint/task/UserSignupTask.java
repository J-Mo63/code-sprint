package com.sudo_code.codesprint.task;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.activity.LoginActivity;

/**
 * Represents an asynchronous registration task used to authenticate
 * the user.
 */
public class UserSignupTask extends AsyncTask<Void, Void, Boolean> {

    // Object fields
    private Activity mAddUserActivity;
    private String mUsername;
    private String mPassword;


    /**
     * UserLoginTask constructor that gets input username and password as well as the calling
     * activity for reference.
     *
     * @param username - the entered username
     * @param password - the entered password
     * @param addUserActivity - the calling activity
     */
    public UserSignupTask(String username, String password, Activity addUserActivity) {
        mUsername = username;
        mPassword = password;
        mAddUserActivity = addUserActivity;
    }

    /**
     * Performs operations before backround task begins like hiding the forms or cancelling
     * the task.
     */
    @Override
    protected void onPreExecute() {
        // Hide the login form
        showSignupForm(false);

        // Cancel the task if there are no stored details
        if (mUsername == null) {
            this.cancel(true);
            showSignupForm(true);
        }
    }


    /**
     * Attempts to sign the user up
     *
     * @param params - nothing at all
     * @return boolean - denoting whether signup attempt successful
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

        boolean valid = true;

        return valid;
    }

    /**
     * Either returns to signup form or opens login form depending on whether
     * the signup attempt is successful.
     *
     * @param success - boolean denoting whether signup attempt successful
     */
    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            // Open to the login activity
            Intent intent = new Intent(mAddUserActivity, LoginActivity.class);
            Toast.makeText(mAddUserActivity.getApplicationContext(),
                    R.string.signed_up_notification,
                    Toast.LENGTH_LONG).show();
            mAddUserActivity.startActivity(intent);
            mAddUserActivity.finish();
        }
        else {
            // Display login form again
            Toast.makeText(mAddUserActivity.getApplicationContext(),
                    R.string.error_cant_signup,
                    Toast.LENGTH_SHORT).show();
            showSignupForm(true);
        }
    }

    /**
     * Shows and hides the login form / progress bar.
     *
     * @param show - whether to show or not
     */
    private void showSignupForm(boolean show) {
        int visibilityOne = (show ? View.GONE : View.VISIBLE);
        int visibilityTwo = (show ? View.VISIBLE : View.GONE);

        mAddUserActivity.findViewById(R.id.add_user_description_textview).setVisibility(visibilityTwo);
        mAddUserActivity.findViewById(R.id.add_user_form).setVisibility(visibilityTwo);
        mAddUserActivity.findViewById(R.id.add_user_progress).setVisibility(visibilityOne);
    }
}
