package com.sudo_code.codesprint.activity;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.model.Challenge;
import java.util.ArrayList;
import static com.sudo_code.codesprint.task.FetchChallengesTask.CHALLENGES;
public class ChallengeActivity extends Activity {

    // UI fields
    private Chronometer mTimer;
    private TextView mQuestionText;
    private EditText mAnswerField;

    // Object fields
    private ArrayList<Challenge> mChallenges;
    private Challenge mCurrentChallenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        mChallenges = getIntent().getParcelableArrayListExtra(CHALLENGES);

        mTimer = (Chronometer) findViewById(R.id.challenge_time);
        mQuestionText = (TextView) findViewById(R.id.challenge_question);
        mAnswerField = (EditText) findViewById(R.id.challenge_answer);
        Button mSubmitButton = (Button) findViewById(R.id.challenge_submit_button);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitAnswer();
            }
        });

        getChallenge();
        mTimer.start();
    }

    private void submitAnswer() {
        if (mCurrentChallenge.getAnswer().equals(mAnswerField.getText().toString())) {
            if (mChallenges.size() > 0) {
                Toast.makeText(this, R.string.answer_correct_notification, Toast.LENGTH_SHORT).show();
                mAnswerField.setText("");
                getChallenge();
            }
            else {
                mTimer.stop();
                Toast.makeText(this, "You win!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, R.string.answer_incorrect_notification, Toast.LENGTH_SHORT).show();
            mAnswerField.setText("");
        }
    }

    private void getChallenge() {
        mCurrentChallenge = mChallenges.get(0);
        mChallenges.remove(mCurrentChallenge);
        mQuestionText.setText(mCurrentChallenge.getQuestion());
    }

}
