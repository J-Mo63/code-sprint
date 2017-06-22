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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.holder.UserChallengeHolder;
import com.sudo_code.codesprint.model.UserChallenge;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.sudo_code.codesprint.activity.LoginActivity.USER_ID;
import static com.sudo_code.codesprint.holder.ChallengeHolder.USER_CHALLNGE_DATE;
import static com.sudo_code.codesprint.holder.ChallengeHolder.USER_CHALLNGE_GRADE;
import static com.sudo_code.codesprint.holder.ChallengeHolder.USER_CHALLNGE_TIME;
import static com.sudo_code.codesprint.task.DatabaseController.USER_CHALLENGE_DB_REF;
import static com.sudo_code.codesprint.task.DatabaseController.USER_DB_REF;
import static com.sudo_code.codesprint.task.DatabaseController.USER_FOLLOW_FIELD_NAME;

/**
 * A screen that displays the results of a past challenge completed by
 * themselves and any users that have completed it that they follow.
 */
public class UserChallengeActivity extends AppCompatActivity {

    // Object fields
    FirebaseRecyclerAdapter mAdapter;

    /**
     * Sets up the toolbar, defines the recycler, gets objects and populates it.
     *
     * @param savedInstanceState - the saved bundle state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appbar_user_challenge_layout);

        Bundle extras = getIntent().getExtras();

        // Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getDate());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView mRecycler = (RecyclerView) findViewById(R.id.user_challenge_recycler);
        TextView mTimeText = (TextView) findViewById(R.id.activity_user_time);
        TextView mGradeText = (TextView) findViewById(R.id.activity_user_grade);

        final LinearLayout progressLayout = (LinearLayout) findViewById(R.id.user_challenge_progress_layout);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.user_challenge_progress_bar);
        final TextView progressText = (TextView) findViewById(R.id.user_challenge_progress_text);

        String time = formatTime(extras.getLong(USER_CHALLNGE_TIME));
        mTimeText.setText(time);
        mGradeText.setText(getIntent().getStringExtra(USER_CHALLNGE_GRADE));

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String uId = sharedPrefs.getString(USER_ID, null);

        // Set up db refs
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userFollowDb = db.child(USER_DB_REF).child(uId)
                .child(USER_FOLLOW_FIELD_NAME);
        DatabaseReference userChallengeDb = db.child(USER_CHALLENGE_DB_REF)
                .child(getIntent().getStringExtra(USER_CHALLNGE_DATE));

        // Set up indexed recycler adapter for Firebase
        mAdapter = new FirebaseIndexRecyclerAdapter<UserChallenge, UserChallengeHolder>(
                UserChallenge.class, R.layout.user_challenge_item, UserChallengeHolder.class,
                userFollowDb, userChallengeDb) {
            @Override
            public void populateViewHolder(UserChallengeHolder holder, UserChallenge userChallenge, int position) {
                holder.setComponents(userChallenge);
            }
        };

        // Check to see if data was loaded and make changes
        db.child(USER_CHALLENGE_DB_REF).child(getIntent().getStringExtra(USER_CHALLNGE_DATE))
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
                progressText.setText(R.string.load_data_error);
            }
        });

        mRecycler.setAdapter(mAdapter);
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
     * Cleans up the recycler adapter and shuts down the connection to the database
     * after the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    /**
     * A helper method to format longs into strings with the appropriate formatting.
     *
     * @param time - the long to be formatted
     * @return String - the formatted string
     */
    private String formatTime(long time) {
        double d = ((double) time)/1000;
        return String.format(Locale.UK, "%.2f", d) + "s";
    }

    /**
     * A helper method to format a date string.
     *
     * @return String - the formatted date
     */
    private String getDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd", Locale.ENGLISH);
        String dateString;
        try {
            Date date = formatter.parse(getIntent().getStringExtra(USER_CHALLNGE_DATE));
            formatter.applyPattern("EEEE d MMM yyyy");
            dateString = formatter.format(date);
        }
        catch (ParseException e) {
            dateString = getIntent().getStringExtra(USER_CHALLNGE_DATE);
        }
        return dateString;
    }
}
