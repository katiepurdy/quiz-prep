package com.katiepurdy.quizprep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ScoreActivity extends Activity {
    private static final String TAG = "Score Activity";
    private final ColourWheel mColourWheel = new ColourWheel();
    private TextView mMessage;
    private TextView mScoreDisplay;
    private Button mTryAgain;
    private int finalScore;
    private String firstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        mMessage = (TextView) findViewById(R.id.congratulatory_textView);
        mScoreDisplay = (TextView) findViewById(R.id.final_score_textView);
        mTryAgain = (Button) findViewById(R.id.try_again_button);
        Bundle extras = getIntent().getExtras();
        firstName = (String) extras.get("name");
        finalScore = (Integer) extras.get("finalScore");

        displayMessage();
        displayFinalScore();

        mTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the main activity
                Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }

    /**
     * Sets the congratulatory message to the display
     */
    public void displayMessage() {
        if (finalScore == 0) {
            mMessage.setText("Too bad, " + firstName + ".");
        } else if (finalScore > 0 && finalScore <= 5) {
            mMessage.setText("Good job, " + firstName + ".");
        } else if (finalScore > 5 && finalScore <= 9) {
            mMessage.setText("Excellent job, " + firstName + "!");
        } else if (finalScore == 10) {
            mMessage.setText("Wow! Congrats, " + firstName + "!\nA perfect score.");
        }
    }

    /**
     * Sets the user's score to the display
     */
    public void displayFinalScore() {
        if (finalScore == 1) {
            mScoreDisplay.setText("You correctly answered " + finalScore + " question.");
        } else {
            mScoreDisplay.setText("You correctly answered " + finalScore + " questions.");
        }
    }
}
