package com.katiepurdy.quizprep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {
    private static final String TAG = "Main Activity";
    private final ColourWheel mColourWheel = new ColourWheel();
    private EditText mFirstNameEditText;
    private Button mStartQuizButton;
    private String mFirstName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirstNameEditText = (EditText) findViewById(R.id.first_name_editText);
        mStartQuizButton = (Button) findViewById(R.id.start_quiz_button);

        mStartQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user's name from the EditText
                mFirstName = mFirstNameEditText.getText().toString();

                // Check for a valid user name
                if (isValidName(mFirstName)) {
                    Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                    intent.putExtra("name", mFirstName);
                    // Navigate to the quiz activity
                    startActivity(intent);

                } else {
                    // Display a long toast to let the user know the name is invalid
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Hey! You have a name, don'tcha? Enter it!" +
                            " :)\nNote: Don't include any spaces or apostrophes.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 80);
                    toast.show();
                }
            }
        });
    }

    /**
     * Validates the user's name by checking for only alphanumeric characters
     *
     * @param name the name to be validated
     * @return whether the name is considered valid or not
     */
    private boolean isValidName(String name) {
        String regex = "^[a-zA-Z]+$";
        if (name.matches(regex)) {
            return true;
        } else {
            return false;
        }
    }
}