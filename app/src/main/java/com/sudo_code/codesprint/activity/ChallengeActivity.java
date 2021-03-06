package com.sudo_code.codesprint.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.model.Challenge;
import com.sudo_code.codesprint.model.Result;
import java.util.ArrayList;

import static com.sudo_code.codesprint.activity.BeginChallengeActivity.CHALLENGES;

/**
 * A screen to display and run the challenges. It times the user and passes results on to the
 * following activity.
 */
public class ChallengeActivity extends Activity {

    // Constants
    public static final String RESULTS = "results_tag";

    // UI fields
    private Chronometer mTimer;
    private TextView mContentText;
    private TextView mQuestionText;
    private TextView mQuestionNumberText;
    private EditText mAnswerField;

    // Object fields
    private ArrayList<Challenge> mChallenges;
    private ArrayList<Result> mResults;
    private Challenge mCurrentChallenge;
    private int mQuestionCount;

    /**
     * Sets up onscreen elements, gets downloaded challenges, defines click listeners and
     * starts the timer on the running challenge.
     *
     * @param savedInstanceState - the saved bundle state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        mChallenges = getIntent().getParcelableArrayListExtra(CHALLENGES);
        mResults = new ArrayList<>();

        mQuestionCount = 1;

        mTimer = (Chronometer) findViewById(R.id.challenge_time);
        mContentText = (TextView) findViewById(R.id.challenge_content);
        mQuestionText = (TextView) findViewById(R.id.challenge_question);
        mQuestionNumberText = (TextView) findViewById(R.id.challenge_question_number);
        mAnswerField = (EditText) findViewById(R.id.challenge_answer);
        Button mSubmitButton = (Button) findViewById(R.id.challenge_submit_button);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitAnswer();
            }
        });

        // On submitting answer form through 'enter' key
        mAnswerField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE) {
                    submitAnswer();
                    return true;
                }
                return false;
            }
        });

        getChallenge();
        mTimer.setBase(SystemClock.elapsedRealtime());
        mTimer.start();
    }

    /**
     * A method to handle checking if the answer is correct, and displaying
     * results to the user.
     */
    private void submitAnswer() {
        // If the answer is correct
        if (mCurrentChallenge.getAnswer().equals(mAnswerField.getText().toString())) {
            recordTime();
            mAnswerField.setText("");

            // If there are more Challenges
            if (mChallenges.size() > 0) {
                Toast.makeText(this, R.string.answer_correct_notification, Toast.LENGTH_SHORT).show();
                mQuestionCount++;
                getChallenge();
            }
            // If all Challenges are completed
            else {
                mTimer.stop();
                // Create intent to open challenge and include the downloaded challenges
                Intent resultsIntent = new Intent(this, ResultsActivity.class);
                resultsIntent.putExtra(RESULTS, mResults);

                // Show the results
                startActivity(resultsIntent);
                finish();
            }
        }
        // If it is incorrect
        else {
            Toast.makeText(this, R.string.answer_incorrect_notification, Toast.LENGTH_SHORT).show();
            mAnswerField.setText("");
        }
    }

    /**
     * A method to grab the next open challenge and display it.
     */
    private void getChallenge() {
        mCurrentChallenge = mChallenges.get(0);
        mChallenges.remove(mCurrentChallenge);
        mContentText.setText(mCurrentChallenge.getContent());
        mQuestionText.setText(mCurrentChallenge.getQuestion());
        String questionNumber = getString(R.string.question_count) + mQuestionCount;
        mQuestionNumberText.setText(questionNumber);
    }

    /**
     * A method that record the time taken for each question, storing the results into
     * an ArrayList.
     */
    private void recordTime() {
        long elapsedTime = SystemClock.elapsedRealtime() - mTimer.getBase();

        if (mResults.size() > 0) {
            for (Result result : mResults) {
                elapsedTime = elapsedTime - result.getTime();
            }
        }

        mResults.add(new Result(mCurrentChallenge.getId(), elapsedTime));
    }
}
