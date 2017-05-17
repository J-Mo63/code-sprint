package com.sudo_code.codesprint.helpers;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.activity.HomeActivity;
import com.sudo_code.codesprint.activity.LoginActivity;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private Activity mLoginActivity;

    private final String mUsername;
    private final String mPassword;

    public UserLoginTask(String username, String password, LoginActivity loginActivity) {
        mUsername = username;
        mPassword = password;
        mLoginActivity = loginActivity;
    }

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
            Toast.makeText(mLoginActivity.getApplicationContext(),
                    R.string.error_incorrect_details,
                    Toast.LENGTH_SHORT).show();
            mLoginActivity.findViewById(R.id.login_description_textview).setVisibility(View.VISIBLE);
            mLoginActivity.findViewById(R.id.login_progress).setVisibility(View.GONE);
            mLoginActivity.findViewById(R.id.login_form).setVisibility(View.VISIBLE);
        }
    }

    private boolean isValid(String username, String password) {
        return (username.equals("j-mo") && password.equals("rmsgnu"));
    }
}
