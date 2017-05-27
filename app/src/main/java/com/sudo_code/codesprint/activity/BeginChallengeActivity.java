package com.sudo_code.codesprint.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.task.FetchChallengesTask;

/**
 * A screen that downloads the current challenge and checks if the user is ready to begin.
 */
public class BeginChallengeActivity extends AppCompatActivity {

    /**
     * Sets up onscreen elements and gets the current challenge.
     *
     * @param savedInstanceState - the saved bundle state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appbar_begin_challenge_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.todays_challenge);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FetchChallengesTask fetchTask = new FetchChallengesTask(BeginChallengeActivity.this);
        fetchTask.execute();
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

}
