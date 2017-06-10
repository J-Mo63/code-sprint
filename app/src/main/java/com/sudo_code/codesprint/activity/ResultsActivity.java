package com.sudo_code.codesprint.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.sudo_code.codesprint.R;
import com.sudo_code.codesprint.model.Result;
import java.util.ArrayList;
import java.util.Locale;
import static com.sudo_code.codesprint.activity.ChallengeActivity.RESULTS;

/**
 * A screen to display user results for a particular Challenge.
 */
public class ResultsActivity extends AppCompatActivity {

    // Object fields
    private ArrayList<Result> mResults;


    /**
     * Sets up onscreen elements and defines click listeners.
     *
     * @param savedInstanceState - the saved bundle state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appbar_results_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.results_bar_title);
        setSupportActionBar(toolbar);

        mResults = getIntent().getParcelableArrayListExtra(RESULTS);

        TextView questionOneResult = (TextView) findViewById(R.id.results_time_one);
        TextView questionTwoResult = (TextView) findViewById(R.id.results_time_two);
        TextView questionThreeResult = (TextView) findViewById(R.id.results_time_three);
        TextView resultTotal = (TextView) findViewById(R.id.results_time_total);
        TextView gradeText = (TextView) findViewById(R.id.results_grade);
        Button doneButton = (Button) findViewById(R.id.results_done_button);

        questionOneResult.setText(formatTime(mResults.get(0).getTime()));
        questionTwoResult.setText(formatTime(mResults.get(1).getTime()));
        questionThreeResult.setText(formatTime(mResults.get(2).getTime()));

        resultTotal.setText(formatTime(getTotal()));
        gradeText.setText(calculateGrade());

        final Intent homeIntent = new Intent(this, HomeActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(homeIntent);
                finish();
            }
        });

//        Post the results to server
//        new postResultsTask(mResults);
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
     * A method that calculates the user's grade for the completed challenges.
     *
     * @return String - the grade value
     */
    private String calculateGrade() {
        return "B";
    }

    /**
     * A method to calculate the total time taken to complete the set of Challenges.
     *
     * @return long - total time taken
     */
    private long getTotal() {
        long total = 0;
        for (Result result : mResults) {
            total += result.getTime();
        }
        return total;
    }

}
