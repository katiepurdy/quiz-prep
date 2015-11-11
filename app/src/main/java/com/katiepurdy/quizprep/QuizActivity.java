package com.katiepurdy.quizprep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;


public class QuizActivity extends Activity {
    private static final String TAG = "Quiz Activity";
    private final ColourWheel mColourWheel = new ColourWheel();
    private ArrayList<HashMap> mQuestionsAndAnswers;
    private ArrayList<String> options;
    private RelativeLayout relativeLayout;
    private TextView mCurrentQuestionDisplay;
    private Button mOptionOne;
    private Button mOptionTwo;
    private Button mOptionThree;
    private Button mOptionFour;
    private Button mRestartQuiz;
    private Button mNext;
    private Button correctButton;
    private String mCurrentQuestion;
    private String mCorrectAnswer = "";
    private String mChosenAnswer = "";
    private int mNumQuestions = 0;
    private int mScore = 0;
    private int mPercentComplete;
    private int mCurrentQuestionIndex = 0;
    private ProgressBar mProgressBar;
    private boolean firstQuestion = true;
    private HashMap<String, String> qAndAPair;
    private String firstName;
    private long seed;
    private int colour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        mCurrentQuestionDisplay = (TextView) findViewById(R.id.question_textView);
        mOptionOne = (Button) findViewById(R.id.option_one_button);
        mOptionTwo = (Button) findViewById(R.id.option_two_button);
        mOptionThree = (Button) findViewById(R.id.option_three_button);
        mOptionFour = (Button) findViewById(R.id.option_four_button);
        mRestartQuiz = (Button) findViewById(R.id.restart_quiz_button);
        mNext = (Button) findViewById(R.id.next_button);
        mProgressBar = (ProgressBar) findViewById(R.id.quiz_completion_progressBar);
        Bundle extras = getIntent().getExtras();
        firstName = (String) extras.get("name");
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);


        // Read question and answer data from text file
        readQuestionsFromFile();
        // Count the number of questions that were read from the text file
        mNumQuestions = getNumQuestions();
        // Load up the options array with possible answers
        loadOptions();
        // Load the first question onto the screen
        loadQuestion();

        // Set OnClickListeners
        mOptionOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChosenAnswer = mOptionOne.getText().toString();
                checkAnswer();
            }
        });

        mOptionTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChosenAnswer = mOptionTwo.getText().toString();
                checkAnswer();
            }
        });

        mOptionThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChosenAnswer = mOptionThree.getText().toString();
                checkAnswer();
            }
        });

        mOptionFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChosenAnswer = mOptionFour.getText().toString();
                checkAnswer();
            }
        });

        mRestartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizActivity.this, MainActivity.class);
                // Reset the score
                mScore = 0;
                // Reset progress
                mProgressBar.setProgress(0);
                // Reshuffle the questions
                shuffleList(mQuestionsAndAnswers);
                // Navigate to the main activity start screen
                startActivity(intent);
                // Reset variables that keep track of question progress
                firstQuestion = true;
                mCurrentQuestionIndex = 0;
                mNumQuestions = 0;
                mQuestionsAndAnswers.clear();
                mCurrentQuestion = "";
                mCurrentQuestionIndex = 0;
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 increaseProgress();
                 loadQuestion();
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }

    /**
     * Method that rearranges the order of items in an ArrayList
     * so that the questions and answers will appear in a different order during
     * the following round.
     *
     * @param list the list to shuffle
     */
    private void shuffleList(ArrayList list) {
        seed = System.nanoTime();
        Collections.shuffle(list, new Random(seed));
    }

    /**
     * Increases the progress of the progress bar
     */
    private void increaseProgress() {
        // Calculate current progress
        mPercentComplete = (mScore / mNumQuestions * 10);
        // Set progress bar based on this newly calculated value
        mProgressBar.setProgress(mPercentComplete);
    }

    /**
     * Compares the chosen answer with the correct answer
     *
     * @return whether or not the answer is correct
     *
     */
    private boolean isCorrect() {
        return mCorrectAnswer.equals(mChosenAnswer);
    }

    /**
     * Increases the score by one
     */
    private void increaseScore() {
        mScore++;
    }

    /**
     * Gets the number of items in the ArrayList of questions and answers
     *
     * @return the number of questions in the current quiz
     *
     */
    private int getNumQuestions() {
        return mQuestionsAndAnswers.size();
    }

    /**
     * Parses the file data and adds it to the ArrayList of HashMaps that
     * contain question and answer pairs.
     */
    private void readQuestionsFromFile() {
        ArrayList<HashMap> tempList = new ArrayList<HashMap>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new
                    InputStreamReader(getApplicationContext().getAssets().open("fileInput.txt")));
            String currentLine = reader.readLine();

            // Break the line into two chunks
            while (currentLine != null) {
                StringTokenizer tokenizer = new StringTokenizer(currentLine, ":");
                String question = tokenizer.nextToken();
                String answer = tokenizer.nextToken();
                // Create a HashMap to hold the question
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("question", question);
                map.put("answer", answer);
                // Add map to the ArrayList
                tempList.add(map);

                currentLine = reader.readLine();
            }
        } catch (Exception e) {
            // Log an error message to the console
            Log.e(TAG, e.getLocalizedMessage());

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    // Log an error message to the console
                    Log.e(TAG, e.getLocalizedMessage());
                }
            }
        }
        mQuestionsAndAnswers = tempList;
        shuffleList(mQuestionsAndAnswers);
    }

    /**
     * Checks whether an answer is correct, enables the "next" button,
     * increases the score, increases the progress on the progress bar
     */
    private void checkAnswer() {
        if (isCorrect()) {
            // Display a "correct" toast to the user
            Toast successToast = Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_LONG);
            successToast.setGravity(Gravity.BOTTOM, 0, 165);
            successToast.show();

            // Enable the "next" button
            mNext.setClickable(true);
            mNext.setVisibility(View.VISIBLE);
            increaseScore();
            // Disable buttons so that score won't increase any more
            disableOptionButtons();
            hideIncorrectAnswers(correctButton);
        } else {
            // Display an "incorrect" toast to the user
            Toast wrongToast = Toast.makeText(getApplicationContext(), "Incorrect!", Toast.LENGTH_LONG);
            wrongToast.setGravity(Gravity.BOTTOM, 0, 165);
            wrongToast.show();
            // Disable buttons so that score won't increase any more
            disableOptionButtons();
            hideIncorrectAnswers(correctButton);
            // Enable the "next" button
            mNext.setClickable(true);
            mNext.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Loads the next question onto the screen
     */
    private void loadQuestion() {

        // If there are unanswered questions remaining
        if (firstQuestion || (mCurrentQuestionIndex < mNumQuestions)) {
            unHideAllOptions();
            firstQuestion = false;
            enableOptionButtons();
            // Disable "next" button
            mNext.setClickable(false);
            mNext.setVisibility(View.INVISIBLE);
            // Get the question and answer pair at next index in the ArrayList
            qAndAPair = mQuestionsAndAnswers.get(mCurrentQuestionIndex);
            // Set the current question
            mCurrentQuestion = qAndAPair.get("question");
            mCurrentQuestionDisplay.setText(mCurrentQuestion);
            // Set the correct answer to this question
            mCorrectAnswer = qAndAPair.get("answer");
            // Assign to random button
            assignButtonAnswers();
            mCurrentQuestionIndex++;
        } else {
            // Go to the score activity
            Intent intent = new Intent(QuizActivity.this, ScoreActivity.class);
            intent.putExtra("name", firstName);
            intent.putExtra("finalScore", mScore);
            startActivity(intent);
        }

    }

    /**
     * Loads the options array with all of the answer values from the ArrayList
     * of question and answer pairs later to be randomly selected from for
     * button order
     */
    private void loadOptions() {
        HashMap<String, String> tempHash = new HashMap<String, String>();
        options = new ArrayList<String>();
        // For every question in the ArrayList
        for (int i = 0; i < mNumQuestions; i++) {
            // Grab the answer and put it into the array
            tempHash = mQuestionsAndAnswers.get(i);
            options.add(tempHash.get("answer"));
        }
    }

    /**
     * Determines the order of possible answers displayed on the screen and
     * ensures that the position of the correct answer isn't always the
     * same from question to question.
     */
    private void assignButtonAnswers() {
        setColours();
        try {
            // Shuffle the ArrayList of options
            shuffleList(options);

            // Ensure that one of the first four items contains the correct answer
            if ((options.get(1)).equals(mCorrectAnswer) ||
                    (options.get(2)).equals(mCorrectAnswer) ||
                    (options.get(3)).equals(mCorrectAnswer) ||
                    (options.get(4)).equals(mCorrectAnswer)) {

                // Assign each button an option from the list
                mOptionOne.setText(options.get(1));
                mOptionTwo.setText(options.get(2));
                mOptionThree.setText(options.get(3));
                mOptionFour.setText(options.get(4));

                // Get the button that has the correct answer to later disable incorrect options
                if (options.get(1).equals(mCorrectAnswer)) {
                    correctButton = mOptionOne;
                } else if (options.get(2).equals(mCorrectAnswer)) {
                    correctButton = mOptionTwo;
                } else if (options.get(3).equals(mCorrectAnswer)) {
                    correctButton = mOptionThree;
                } else if (options.get(4).equals(mCorrectAnswer)) {
                    correctButton = mOptionFour;
                }

            } else {
                assignButtonAnswers();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Disables buttons
     */
    private void disableOptionButtons() {
        mOptionOne.setClickable(false);
        mOptionTwo.setClickable(false);
        mOptionThree.setClickable(false);
        mOptionFour.setClickable(false);
    }

    /**
     * Enables buttons
     */
    private void enableOptionButtons() {
        mOptionOne.setClickable(true);
        mOptionTwo.setClickable(true);
        mOptionThree.setClickable(true);
        mOptionFour.setClickable(true);
    }

    /**
     * Changes the visibility of incorrect answers to invisible
     *
     * @param correct the Button containing the correct answer
     */
    private void hideIncorrectAnswers(Button correct) {
        int correctButton = correct.getId();
        final int buttonOneID = mOptionOne.getId();
        final int buttonTwoID = mOptionTwo.getId();
        final int buttonThreeID = mOptionThree.getId();
        final int buttonFourID = mOptionFour.getId();

        if (correct.getId() == buttonOneID) {
            correctButton = 1;
        } else if (correct.getId() == buttonTwoID) {
            correctButton = 2;
        } else if (correct.getId() == buttonThreeID) {
            correctButton = 3;
        } else  if (correct.getId() == buttonFourID) {
            correctButton = 4;
        }

        switch (correctButton) {
            case 1:
                mOptionTwo.setVisibility(View.INVISIBLE);
                mOptionThree.setVisibility(View.INVISIBLE);
                mOptionFour.setVisibility(View.INVISIBLE);
                break;
            case 2:
                mOptionOne.setVisibility(View.INVISIBLE);
                mOptionThree.setVisibility(View.INVISIBLE);
                mOptionFour.setVisibility(View.INVISIBLE);
                break;
            case 3:
                mOptionOne.setVisibility(View.INVISIBLE);
                mOptionTwo.setVisibility(View.INVISIBLE);
                mOptionFour.setVisibility(View.INVISIBLE);
                break;
            case 4:
                mOptionOne.setVisibility(View.INVISIBLE);
                mOptionTwo.setVisibility(View.INVISIBLE);
                mOptionThree.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }

    }

    /**
     * Sets the visibility of all option buttons to visible
     */
    private void unHideAllOptions() {
        mOptionOne.setVisibility(View.VISIBLE);
        mOptionTwo.setVisibility(View.VISIBLE);
        mOptionThree.setVisibility(View.VISIBLE);
        mOptionFour.setVisibility(View.VISIBLE);
    }

    /**
     * Sets the proper text and background colour of Views
     */
    private void setColours() {
        colour = mColourWheel.getColour();
        relativeLayout.setBackgroundColor(colour);
        mOptionOne.setTextColor(colour);
        mOptionTwo.setTextColor(colour);
        mOptionThree.setTextColor(colour);
        mOptionFour.setTextColor(colour);
        mRestartQuiz.setTextColor(colour);
        mNext.setTextColor(colour);
    }
}
