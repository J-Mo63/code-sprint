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

public class ResultsActivity extends AppCompatActivity {

    // Object fields
    private ArrayList<Result> mResults;

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

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(homeIntent);
            }
        });

//        Post the results to server
//        new postResultsTask(mResults);
    }

    private String formatTime(long time) {
        double d = ((double) time)/1000;
        return String.format(Locale.UK, "%.2f", d) + "s";
    }

    private String calculateGrade() {
        return "B";
    }

    private long getTotal() {
        long total = 0;
        for (Result result : mResults) {
            total += result.getTime();
        }
        return total;
    }

}
