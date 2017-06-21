package com.sudo_code.codesprint.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.adapter.UserFollowHolder;
import com.sudo_code.codesprint.model.User;

import static com.sudo_code.codesprint.activity.LoginActivity.USER_ID;
import static com.sudo_code.codesprint.task.DatabaseController.USER_DB_REF;
import static com.sudo_code.codesprint.task.DatabaseController.USER_FOLLOW_DB_REF;

/**
 * A screen that displays all users being followed by the currently logged in user
 * and allows them to unfollow any of their followed users.
 */
public class FollowingActivity extends AppCompatActivity {

    // Object fields
    private FirebaseRecyclerAdapter mAdapter;

    /**
     * Sets up the toolbar, defines the recycler, gets objects and populates it
     * with a Firebase UI adapter.
     *
     * @param savedInstanceState - the saved bundle state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appbar_following_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.following_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.following_recycler);

        // Set up indexed recycler adapter for Firebase
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        mAdapter = new FirebaseIndexRecyclerAdapter<User, UserFollowHolder>(
                User.class, R.layout.user_item, UserFollowHolder.class,
                db.child(USER_DB_REF).child(getCurrentUserId()).child(USER_FOLLOW_DB_REF),
                db.child(USER_DB_REF)) {
            @Override
            public void populateViewHolder(UserFollowHolder holder, User user, int position) {
                holder.setUsername(user.getUsername());
                holder.setDelete(user.getId(), user.getUsername());
            }
        };

        // Adapter setup
        recycler.setAdapter(mAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL);
        recycler.addItemDecoration(dividerItemDecoration);
    }

    /**
     * Checks which option was selected and performs the approproate actions.
     *
     * @param item - the selected option MenuItem
     * @return boolean - success on normal menu processing (handled by superclass)
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
     * Cleans up the recycler adapter and shuts down the connection to the database
     * after the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    /**
     * A helper method to return the id of the current user from Shared Prferences
     * just incase the session is dropped.
     *
     * @return String - the current user user id
     */
    private String getCurrentUserId() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPrefs.getString(USER_ID, null);
    }
}
