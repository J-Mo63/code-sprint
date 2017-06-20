package com.sudo_code.codesprint.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.adapter.ChallengeAdapter;
import com.sudo_code.codesprint.model.UserChallenge;
import com.sudo_code.codesprint.task.DatabaseController;

import java.util.ArrayList;
import java.util.Date;

/**
 * A home screen that displays past challenges and allows the user to begin
 * today's challenge.
 */
public class HomeActivity extends AppCompatActivity {

    // Object fields
    private ArrayList<UserChallenge> mUserChallenges;
    private DatabaseController mDbController;

    /**
     * Sets up the toolbar, defines the recycler, gets objects and populates it.
     *
     * @param savedInstanceState - the saved bundle state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appbar_home_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.home_recycler_view);

        // Adapter setup
        mUserChallenges = new ArrayList<>();
        getChallenges();
        ChallengeAdapter adapter = new ChallengeAdapter(mUserChallenges);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL);
        recycler.addItemDecoration(dividerItemDecoration);

        // Intent definitions
        final Intent beginChallengeIntent = new Intent(this, BeginChallengeActivity.class);

        // Listeners
        Button currentChallengeButton = (Button) findViewById(R.id.home_challenge_button);
        currentChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(beginChallengeIntent);
            }
        });

        mDbController = new DatabaseController(this);
    }


    /**
     * Inflates the options menu and sets menu item colours.
     *
     * @param menu - The options menu to be inflated.
     * @return boolean - success
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        Drawable drawable = menu.findItem(R.id.action_add_user).getIcon();
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        return true;
    }

    /**
     * Checks which option was selected and performs the approproate actions.
     *
     * @param item - the selected option MenuItem
     * @return boolean - success on normal menu processing (handled by superclass)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_users) {
            Intent followingIntent = new Intent(this, FollowingActivity.class);
            startActivity(followingIntent);
        }
        else if (id == R.id.action_quit) {
            Intent logoutIntent = new Intent(this, LoginActivity.class);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().clear().apply();
            startActivity(logoutIntent);
            finish();
        }
        else if (id == R.id.action_add_user) {
            showAddUserDialogue();
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Populates the challenges field with UserChallenge objects.
     */
    private void getChallenges() {
        mUserChallenges.add(new UserChallenge("j-mo", new Date(), "A", 31.45));
        mUserChallenges.add(new UserChallenge("j-mo", new Date(), "C", 44.15));
        mUserChallenges.add(new UserChallenge("j-mo", new Date(), "B", 25.23));
        mUserChallenges.add(new UserChallenge("j-mo", new Date(), "D", 89.01));
    }


    /**
     * Displays an alert dialogue allowing the user to follow another user.
     */
    protected void showAddUserDialogue() {
        // Setup the view
        LayoutInflater layoutInflater = LayoutInflater.from(HomeActivity.this);
        View promptView = layoutInflater.inflate(R.layout.follow_input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        alertDialogBuilder.setView(promptView);

        final EditText usernameText = (EditText) promptView.findViewById(R.id.follow_username);

        // Setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton(R.string.follow_user_submit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    mDbController.createUserFollow(usernameText.getText().toString());
                    }
                })
                .setNegativeButton(R.string.follow_user_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // Create the alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}