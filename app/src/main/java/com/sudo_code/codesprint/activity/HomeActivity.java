package com.sudo_code.codesprint.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.adapter.ChallengeAdapter;
import com.sudo_code.codesprint.model.UserChallenge;

import java.util.ArrayList;
import java.util.Date;

/**
 * A home screen that displays past challenges and allows the user to begin
 * today's challenge.
 */
public class HomeActivity extends AppCompatActivity {

    // Object fields
    private ArrayList<UserChallenge> mUserChallenges;

    /**
     * Defines the recycler, gets objects and populates it.
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
        RecyclerView mRecycler = (RecyclerView) findViewById(R.id.home_recycler_view);

        // Adapter setup
        getChallenges();
        ChallengeAdapter adapter = new ChallengeAdapter(mUserChallenges);
        mRecycler.setAdapter(adapter);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL);
        mRecycler.addItemDecoration(dividerItemDecoration);

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
     * Populates the challenges field with UserChallenge objects.
     */
    private void getChallenges() {
        mUserChallenges = new ArrayList<>();
        mUserChallenges.add(new UserChallenge("j-mo", new Date(), "A", 31.45));
        mUserChallenges.add(new UserChallenge("j-mo", new Date(), "C", 44.15));
        mUserChallenges.add(new UserChallenge("j-mo", new Date(), "B", 25.23));
        mUserChallenges.add(new UserChallenge("j-mo", new Date(), "D", 89.01));
    }

}
