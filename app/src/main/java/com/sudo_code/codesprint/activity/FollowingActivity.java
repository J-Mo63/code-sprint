package com.sudo_code.codesprint.activity;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.adapter.ChallengeAdapter;
import com.sudo_code.codesprint.adapter.FollowingAdapter;
import com.sudo_code.codesprint.model.User;
import com.sudo_code.codesprint.model.UserChallenge;

import java.util.ArrayList;
import java.util.Date;

/**
 * A screen that displays all users being followed by the currently logged in user
 * and allows them to unfollow any of their followed users.
 */
public class FollowingActivity extends AppCompatActivity {

    // Object fields
    private ArrayList<User> mFollowing;

    /**
     * Sets up the toolbar, defines the recycler, gets objects and populates it.
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

        // Adapter setup
        mFollowing = new ArrayList<>();
        getFollowing();
        FollowingAdapter adapter = new FollowingAdapter(mFollowing);
        recycler.setAdapter(adapter);
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
     * Populates the users field with User objects.
     */
    private void getFollowing() {
        mFollowing.add(new User(1, "j-mo"));
        mFollowing.add(new User(1, "bb3b123"));
        mFollowing.add(new User(1, "dandolo"));
        mFollowing.add(new User(1, "actom360"));
        mFollowing.add(new User(1, "oldie"));
    }

}
