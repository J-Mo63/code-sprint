package com.sudo_code.codesprint.task;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.activity.ChallengeActivity;
import com.sudo_code.codesprint.model.Challenge;
import java.util.ArrayList;

/**
 * Represents an asynchronous task used to retrieve the current challenges from
 * the server.
 */
public class FetchChallengesTask extends AsyncTask<Void, Void, ArrayList<Challenge>> {

    // Constants
    public static final String CHALLENGES = "challenges_tag";

    // Object fields
    private Activity mBeginChallenngeActivity;


    /**
     * FetchChallengesTask constructor that gets the activity for reference.
     *
     * @param beginChallenngeActivity - the calling activity
     */
    public FetchChallengesTask(Activity beginChallenngeActivity) {
        mBeginChallenngeActivity = beginChallenngeActivity;
    }

    /**
     * Notifies the user that the the current challenge is currently downloading.
     */
    @Override
    public void onPreExecute() {
        Toast.makeText(mBeginChallenngeActivity.getApplicationContext(),
                R.string.downloading_challenges_notification, Toast.LENGTH_SHORT).show();
    }


    /**
     * Attempts to fetch the current challenge activities.
     *
     * @param params - nothing at all
     * @return boolean - denoting whether login attempt successful
     */
    @Override
    protected ArrayList<Challenge> doInBackground(Void... params) {
        // TODO: attempt authentication against a network service.

        ArrayList<Challenge> challenges = new ArrayList<>();

        try {
            // Simulate network access.
            Thread.sleep(2500);
            challenges.add(new Challenge(1, "meme 1", "What is 2+2?", "5"));
            challenges.add(new Challenge(2, "meme 2", "Where is home?", "The attic"));
            challenges.add(new Challenge(3, "meme 3", "Where are we reporting live from?", "The 90059"));
        }
        catch (InterruptedException e) {
            return null;
        }

        return challenges;
    }

    /**
     * Will allow nofify the user that the challenges have completed thier downloading and that will
     * set the onclick of the button to start the downloaded challenges, afterwhich it will enable
     * the button.
     *
     * @param challenges ArrayList - list of downloaded challenges
     */
    @Override
    protected void onPostExecute(final ArrayList<Challenge> challenges) {
        Button beginButton = (Button) mBeginChallenngeActivity.findViewById(R.id.begin_challenge_begin_button);

        // Create intent to open challenge and include the downloaded challenges
        final Intent challengesIntent = new Intent(mBeginChallenngeActivity.getApplicationContext(),
                ChallengeActivity.class);
        challengesIntent.putExtra(CHALLENGES, challenges);

        // Add intent to click listener
        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBeginChallenngeActivity.startActivity(challengesIntent);
                mBeginChallenngeActivity.finish();
            }
        });

        // Tell the user it has been downloaded and that they may proceed
        Toast.makeText(mBeginChallenngeActivity.getApplicationContext(),
                R.string.finished_downloading_notification, Toast.LENGTH_SHORT).show();
        beginButton.setEnabled(true);
    }
}
