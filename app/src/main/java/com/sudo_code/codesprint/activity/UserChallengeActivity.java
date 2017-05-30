package com.sudo_code.codesprint.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.adapter.UserChallengeAdapter;
import com.sudo_code.codesprint.model.UserChallenge;

import java.util.ArrayList;
import java.util.Date;

/**
 * A screen that displays the results of a past challenge completed by
 * themselves and any users that have completed it that they follow.
 */
public class UserChallengeActivity extends AppCompatActivity {

    // Object fields
    private ArrayList<UserChallenge> mUserChallenges;

    /**
     * Sets up the toolbar, defines the recycler, gets objects and populates it.
     *
     * @param savedInstanceState - the saved bundle state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appbar_user_challenge_layout);

        // Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Monday");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView mRecycler = (RecyclerView) findViewById(R.id.user_challenge_recycler);

        // Adapter setup
        mUserChallenges = new ArrayList<>();
        getUserChallenges();
        UserChallengeAdapter adapter = new UserChallengeAdapter(mUserChallenges);
        mRecycler.setAdapter(adapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL);
        mRecycler.addItemDecoration(dividerItemDecoration);
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
     * Populates the userChallenges field with UserChallenge objects.
     */
    private void getUserChallenges() {
        mUserChallenges.add(new UserChallenge("dandolo", new Date(), "A", 31.45));
        mUserChallenges.add(new UserChallenge("actom360", new Date(), "C", 44.15));
        mUserChallenges.add(new UserChallenge("bb3b123", new Date(), "B", 25.23));
        mUserChallenges.add(new UserChallenge("palzmadol", new Date(), "D", 89.01));
        mUserChallenges.add(new UserChallenge("peanut", new Date(), "D", 89.01));
        mUserChallenges.add(new UserChallenge("dudewhodoesstuff", new Date(), "D", 89.01));
    }

}
