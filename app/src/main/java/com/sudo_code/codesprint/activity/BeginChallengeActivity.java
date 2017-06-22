package com.sudo_code.codesprint.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.model.Challenge;

import java.util.ArrayList;

import static com.sudo_code.codesprint.helper.DatabaseController.CHALLENGE_DB_REF;

/**
 * A screen that downloads the current challenge and checks if the user is ready to begin.
 */
public class BeginChallengeActivity extends AppCompatActivity {

    private static ArrayList<Challenge> mChallenges;

    // Constants
    public static final String CHALLENGES = "challenges_tag";

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

        final LinearLayout progressLayout = (LinearLayout) findViewById(R.id.begin_challenge_progress_layout);

        // Set up database refernces
        DatabaseReference mDatabaseChallenge = FirebaseDatabase.getInstance().getReference()
                .child(CHALLENGE_DB_REF);

        // Set up components for challenge fetcher
        mChallenges = new ArrayList<>();
        final Activity activity = this;

        // Set up the challenge fetching task
        mDatabaseChallenge.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Challenge challenge = data.getValue(Challenge.class);
                    mChallenges.add(challenge);
                }
                Button beginButton = (Button) findViewById(R.id.begin_challenge_begin_button);

                // Create intent to open challenge and include the downloaded challenges
                final Intent challengesIntent = new Intent(activity, ChallengeActivity.class);
                challengesIntent.putExtra(CHALLENGES, mChallenges);

                // Add intent to click listener
                beginButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(challengesIntent);
                        finish();
                    }
                });

                // Tell the user it has been downloaded and that they may proceed
                progressLayout.setVisibility(View.INVISIBLE);
                beginButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(activity, R.string.error_occured_general,
                        Toast.LENGTH_SHORT).show();
            }
        });
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


//challenges.add(new Challenge(1, "int i = 5;\nint* p = &i;\n*p += 10;\ncout << *p;",
//        "What is output by this code?", "15"));
//        challenges.add(new Challenge(2, "int i = 5;\nint* p = &i;\np += 10;\ncout << &i;",
//        "What is output by this code?", "5"));
//        challenges.add(new Challenge(3, "int main(){\n  string input;\n  cin >> input;\n  cout << input;\n}",
//        "What will the following code print if given the input \"Hello World\"?", "Hello"));