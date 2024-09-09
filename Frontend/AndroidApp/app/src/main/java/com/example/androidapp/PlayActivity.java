package com.example.androidapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.BuildConfig;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;
import java.util.List;

import org.java_websocket.handshake.ServerHandshake;

/**
 * Class that implements all of the aspects of the "trivia game" allowing users to answer questions and receive feedback with their answers.
 */
public class PlayActivity extends AppCompatActivity implements WebSocketScoreListener{

    //this is a cicd change lol

    private WebSocketScore webSocketScoreManager;

    /**
     * Selectable button on screen for question.
     */
    private Button choice1Button;

    /**
     * Selectable button on screen for question.
     */
    private Button choice2Button;

    /**
     * Selectable button on screen for question.
     */

    private Button choice3Button;

    /**
     * Selectable button on screen for question.
     */
    private Button choice4Button;

    /**
     * Displays the current question message.
     */
    private TextView questionTextbox;

    /**
     * Displays Fill in the Blank area
     */
    private EditText fillBlank;

    /**
     * used to submit an answer
     */
    private Button enterButton;

    /**
     * Progress Bar object to keep track of remaining time in a question.
     */
    private ProgressBar timerProgressBar;

    /**
     * variable used to countdown the timer.
     */
    private static final long COUNTDOWN_TIME = 30000; // 30 seconds

    /**
     * CountdownTimer object.
     */
    private CountDownTimer countDownTimer;

    /**
     * stores the text of the correct Answer.
     */
    public String correctAnswer;

    /**
     * queue object to run one volley request at a time.
     */
    private RequestQueue requestQueue;

    /**
     * used to increment through the list of question ids.
     */
    private int questionIndex = -1;  // To keep track of the current question index

    /**
     * object that stores players information
     */
    public JSONObject playerObject;

    /**
     * Url for the connection to the server.
     */
    private static final String BASE_URL = "http://coms-309-041.class.las.iastate.edu:8080";

    public boolean isMultipleChoiceQuestion;
    public boolean isTrueFalseQuestion;
    public boolean isFillBlankQuestion;
    public boolean isSelectQuestion;

    /** select question items */
    private CompoundButton checkboxOption1;
    private CompoundButton checkboxOption2;
    private CompoundButton checkboxOption3;
    private CompoundButton checkboxOption4;
    private Button submitButton;
    public boolean isChecked1;
    public boolean isChecked2;
    public boolean isChecked3;
    public boolean isChecked4;
    public String check1, check2, check3, check4;


    /** score websocket */
    public String serverUrl;
    private int currentScore;
    private EditText scoreBox;


    /** powerup related items */
    private int questionsAnswered;

    /**
     * adds 10 secs to timer
     */
    private TextView freezeText;

    private Button freeze;

    /**
     * doubles points scored for question
     */
    private TextView doublePointsText;

    private Button doublePoints;
    private boolean isDouble = false;

    /**
     * funny powerup ....
     */
    private TextView trollText;

    /**
     * text for powerups
     */
    private TextView powerUpText;

    /**
     * The media player for during the game
     */
    private MediaPlayer mediaPlayer;

    private Button troll;
    private boolean istroll = false;

    /**
     * Holds true if the user is a guest, false otherwise
     */
    private boolean isGuest;

    /**
     * loads the activity xml and view on creation.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamescreen);

        uiDesign.applyRandomBackgroundColor(this);

        currentScore = 0;

        //get the player object
        Intent intent = getIntent();

        isGuest = intent.getBooleanExtra("isGuest", false);

        if (intent.hasExtra("playerObject")) {
            String jsonString = intent.getStringExtra("playerObject");

            try {
                assert jsonString != null;
                playerObject = new JSONObject(jsonString);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        freezeText = findViewById(R.id.freezecount);
        doublePointsText = findViewById(R.id.doublecount);
        trollText = findViewById(R.id.trollcount);

        freeze = findViewById(R.id.freeze);
        doublePoints = findViewById(R.id.doublePoints);
        troll = findViewById(R.id.troll);
        powerUpText = findViewById(R.id.powerupsLabel);

        if(isGuest){
            freezeText.setVisibility(View.INVISIBLE);
            doublePointsText.setVisibility(View.INVISIBLE);
            trollText.setVisibility(View.INVISIBLE);

            freeze.setVisibility(View.INVISIBLE);
            doublePoints.setVisibility(View.INVISIBLE);
            troll.setVisibility(View.INVISIBLE);

            powerUpText.setVisibility(View.INVISIBLE);
        }

        webSocketScoreManager = WebSocketScore.getInstance();
        webSocketScoreManager.setWebSocketScoreListener(this);

        try {
            serverUrl = "ws://coms-309-041.class.las.iastate.edu:8080/score/" + playerObject.getString("playerUsername");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        webSocketScoreManager.connectWebSocket(serverUrl);

        // Initialize UI elements
        choice1Button = findViewById(R.id.choice1Button);
        choice2Button = findViewById(R.id.choice2Button);
        choice3Button = findViewById(R.id.choice3Button);
        choice4Button = findViewById(R.id.choice4Button);

        questionTextbox = findViewById(R.id.questionTextbox);
        timerProgressBar = findViewById(R.id.timerProgressBar);

        fillBlank = findViewById(R.id.editTextFillBlank);
        enterButton = findViewById(R.id.enterButton);

        checkboxOption1 = findViewById(R.id.checkboxOption1);
        checkboxOption2 = findViewById(R.id.checkboxOption2);
        checkboxOption3 = findViewById(R.id.checkboxOption3);
        checkboxOption4 = findViewById(R.id.checkboxOption4);
        submitButton = findViewById(R.id.submitBtn);

        scoreBox = findViewById(R.id.ScoreBox);

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Initialize click listeners for choice buttons
        choice1Button.setOnClickListener(new View.OnClickListener() {
            /**
             * selects the first question option.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                try {
                    handleAnswer(choice1Button.getText().toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        choice2Button.setOnClickListener(new View.OnClickListener() {
            /**
             * selects the second question option.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                try {
                    handleAnswer(choice2Button.getText().toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        choice3Button.setOnClickListener(new View.OnClickListener() {
            /**
             * selects the third question option.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                try {
                    handleAnswer(choice3Button.getText().toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        choice4Button.setOnClickListener(new View.OnClickListener() {
            /**
             * selects the fourth question option.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                try {
                    handleAnswer(choice4Button.getText().toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    handleAnswer(fillBlank.getText().toString());
                } catch (JSONException e){
                    throw new RuntimeException(e);
                }
            }
        });

        checkboxOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked1){
                    isChecked1 = false;
                }else{
                    isChecked1 = true;
                }
            }
        });

        checkboxOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked2){
                    isChecked2 = false;
                }else{
                    isChecked2 = true;
                }
            }
        });

        checkboxOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked3){
                    isChecked3 = false;
                }else{
                    isChecked3 = true;
                }
            }
        });

        checkboxOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked4){
                    isChecked4 = false;
                }else{
                    isChecked4 = true;
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(isChecked1){
                    check1 = "true";
                }else{
                    check1 = "false";
                }

                if(isChecked2){
                    check2 = "true";
                }else{
                    check2 = "false";
                }

                if(isChecked3){
                    check3 = "true";
                }else{
                    check3 = "false";
                }

                if(isChecked4){
                    check4 = "true";
                }else{
                    check4 = "false";
                }

               String selectedAnswers = check1 + check2 + check3 + check4;

                try{
                    handleAnswer(selectedAnswers);

                    isChecked1 = false;
                    isChecked2 = false;
                    isChecked3 = false;
                    isChecked4 = false;

                    checkboxOption1.setChecked(false);
                    checkboxOption2.setChecked(false);
                    checkboxOption3.setChecked(false);
                    checkboxOption4.setChecked(false);

                } catch (JSONException e){
                    throw new RuntimeException(e);
                }
            }
        });

        /** powerup buttons */

        freeze.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (fcount > 0) {
                    try {
                        usefreezePowerup(PlayActivity.this, playerObject.getString("playerUsername"));
                        pauseTimer();

                        getUserPowerups(PlayActivity.this, playerObject.getString("playerUsername"));
                        freezeText.setText(freezeCount);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    Toast.makeText(PlayActivity.this, "No freeze powerups left!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        doublePoints.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (dcount > 0) {
                    try {
                        usetwoTimesPowerup(PlayActivity.this, playerObject.getString("playerUsername"));
                        isDouble = true;

                        getUserPowerups(PlayActivity.this, playerObject.getString("playerUsername"));
                        doublePointsText.setText(timesTwoCount);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    Toast.makeText(PlayActivity.this, "No double point powerups left!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        troll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (tcount > 0) {
                    try {
                        usetrollPowerup(PlayActivity.this, playerObject.getString("playerUsername"));

                        Random random = new Random();
                        int gamble = random.nextInt(2);

                        if(gamble == 0){
                            Toast.makeText(PlayActivity.this, "OOOH you Lost!", Toast.LENGTH_SHORT).show();
                            currentScore = 0;
                            scoreBox.setText("Score:" + currentScore);
                        }else{
                            Toast.makeText(PlayActivity.this, "Winner!", Toast.LENGTH_SHORT).show();
                            currentScore *= 2;
                            scoreBox.setText("Score:" + currentScore);
                        }

                        getUserPowerups(PlayActivity.this, playerObject.getString("playerUsername"));
                        trollText.setText(trollCount);

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                } else {
                    Toast.makeText(PlayActivity.this, "No trolls left!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mediaPlayer = MediaPlayer.create(PlayActivity.this, R.raw.play);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        fetchQuestion();
    }

    /**
     * selects the question from the database using volley
     */
    public void fetchQuestion() {
        fillBlank.setText("");

        // Get the selected question type from the intent
        int questionType = getIntent().getIntExtra("selectedQuestionType", 0); // Default to 0 which is Random

        // Handle random separately
        if (questionType == 0) { // Assuming Random is at index 0
            Random random = new Random();
            questionType = random.nextInt(3) + 1;
        }

        // Adjusted to account for 'Random' being index 0
        switch (questionType) {
            case 1:
                isMultipleChoiceQuestion = true;
                isTrueFalseQuestion = false;
                isFillBlankQuestion = false;
                isSelectQuestion = false;
                fetchMultipleChoiceQuestion();
                break;
            case 2:
                isMultipleChoiceQuestion = false;
                isTrueFalseQuestion = false;
                isFillBlankQuestion =false;
                isSelectQuestion = true;
                fetchSelectQuestion();
                break;
            case 3:
                isMultipleChoiceQuestion = false;
                isTrueFalseQuestion = true;
                isSelectQuestion = false;
                isFillBlankQuestion = false;
                fetchTrueFalseQuestion();
                break;
            case 4:
                isMultipleChoiceQuestion = false;
                isTrueFalseQuestion = false;
                isSelectQuestion = false;
                isFillBlankQuestion = true;
                fetchfillBlankQuestion();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + questionType);
        }
    }

    public void fetchMultipleChoiceQuestion() {
        String url = BASE_URL + "/multiplechoicequestions";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle the JSON array response and update UI
                        handleQuestionsResponse(response, true,false,false,false,false); // Pass true for multiple choice
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error fetching multiple choice questions";
                        if (error != null && error.getMessage() != null) {
                            errorMessage += ": " + error.getMessage();
                        }
                        Log.e("PlayActivity", errorMessage);
                        Toast.makeText(PlayActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(request);
    }

    public void fetchTrueFalseQuestion() {
        String url = BASE_URL + "/truefalse";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle the JSON array response and update UI
                        handleQuestionsResponse(response, false,false,false,false,true); // Pass false for true/false
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error fetching true/false questions";
                        if (error != null && error.getMessage() != null) {
                            errorMessage += ": " + error.getMessage();
                        }
                        Log.e("PlayActivity", errorMessage);
                        Toast.makeText(PlayActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(request);
    }

    public void fetchfillBlankQuestion() {
        String url = BASE_URL + "/fillblanks";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle the JSON array response and update UI
                        handleQuestionsResponse(response, false,true,false,false,false); // Pass false for fill in the blank
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error fetching fill in the blank questions";
                        if (error != null && error.getMessage() != null) {
                            errorMessage += ": " + error.getMessage();
                        }
                        Log.e("PlayActivity", errorMessage);
                        Toast.makeText(PlayActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(request);
    }

    public void fetchSelectQuestion(){
        String url = BASE_URL + "/select";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Handle the JSON array response and update UI
                        handleQuestionsResponse(response, false,false,true,false,false); // Pass false for fill in the blank
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage = "Error fetching checkbox select questions";
                        if (error != null && error.getMessage() != null) {
                            errorMessage += ": " + error.getMessage();
                        }
                        Log.e("PlayActivity", errorMessage);
                        Toast.makeText(PlayActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(request);
    }

    public int lastIndex;
    /**
     * updates the ui with the loaded questions from the JSON Array.
     * @param questions - Array object of questions fetched from the database.
     */
    public void handleQuestionsResponse(JSONArray questions, boolean isMultipleChoice, boolean isFillBlankQuestion, boolean isSelectQuestion, boolean isPutInOrderQuestion, boolean isTrueFalseQuestion) {
       if(!isGuest) {updatePowerUpsUI();}

        runOnUiThread(() -> {

            if (questions.length() > 0) {

                Random random = new Random();
                int questionIndex = random.nextInt(questions.length());
                JSONObject questionObject = questions.optJSONObject(questionIndex);

                // Keep the last index to compare against.
                lastIndex = questionIndex; // This needs to be stored between calls to this method.

                // Attempt to find a different question if possible
                int attempts = 0;
                while (attempts < questions.length() * 2) { // was * 2
                    int nextIndex = random.nextInt(questions.length());

                    if (nextIndex != lastIndex) { // Check to avoid immediate repetition
                        JSONObject nextObject = questions.optJSONObject(nextIndex);

                        if (!nextObject.equals(questionObject)) {
                            questionObject = nextObject;
                            lastIndex = nextIndex; // Update lastIndex to the new index
                            break;
                        }
                    }
                    attempts++;
                }

                if (isMultipleChoice) {
                        updateUIWithMultipleChoiceQuestion(questionObject);
                } else if (isTrueFalseQuestion) {
                        updateUIWithTrueFalseQuestion(questionObject);
                } else if (isFillBlankQuestion) {
                        updateUIWithfillBlankQuestion(questionObject);
                } else if (isSelectQuestion) {
                        updateUIWithSelectQuestion(questionObject);
                }

            } else {
                Toast.makeText(PlayActivity.this, "No questions available", Toast.LENGTH_SHORT).show();
            }
            resetTimer();
        });
    }

    /**
     * updates the ui with the collected information from handleQuestionResponse
     * @param question - json object received from handleQuestionResponse
     */
    public void updateUIWithMultipleChoiceQuestion(JSONObject question) {
        try {
            String questionText = question.getString("question");
            String answer1 = question.getString("answer1");
            String answer2 = question.getString("answer2");
            String answer3 = question.getString("answer3");
            String answer4 = question.getString("answer4");

            List<String> choices = new ArrayList<>();
            choices.add(answer1);
            choices.add(answer2);
            choices.add(answer3);
            choices.add(answer4);

            // Shuffle the choices
            Collections.shuffle(choices);

            choice1Button.setVisibility(View.VISIBLE);
            choice2Button.setVisibility(View.VISIBLE);
            choice3Button.setVisibility(View.VISIBLE);
            choice4Button.setVisibility(View.VISIBLE);

            choice1Button.setClickable(true);
            choice2Button.setClickable(true);
            choice3Button.setClickable(true);
            choice4Button.setClickable(true);

            fillBlank.setVisibility(View.INVISIBLE);
            enterButton.setVisibility(View.INVISIBLE);

            fillBlank.setClickable(false);
            enterButton.setClickable(false);

            checkboxOption1.setVisibility(View.INVISIBLE);
            checkboxOption2.setVisibility(View.INVISIBLE);
            checkboxOption3.setVisibility(View.INVISIBLE);
            checkboxOption4.setVisibility(View.INVISIBLE);
            submitButton.setVisibility(View.INVISIBLE);

            checkboxOption1.setClickable(false);
            checkboxOption2.setClickable(false);
            checkboxOption3.setClickable(false);
            checkboxOption4.setClickable(false);
            submitButton.setClickable(false);

            questionTextbox.setText(questionText);
            choice1Button.setText(choices.get(0));
            choice2Button.setText(choices.get(1));
            choice3Button.setText(choices.get(2));
            choice4Button.setText(choices.get(3));

            correctAnswer = getCorrectAnswer(question);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("PlayActivity", "Error parsing question data: " + e.getMessage());
        }
    }

    /**
     * updates buttons for true or false questions
     * @param question
     */
    public void updateUIWithTrueFalseQuestion(JSONObject question) {
        try {
            String questionText = question.getString("question");

            questionTextbox.setText(questionText);
            choice1Button.setText("True");
            choice2Button.setText("False");

            // Make sure to make the buttons visible
            choice1Button.setVisibility(View.VISIBLE);
            choice2Button.setVisibility(View.VISIBLE);

            choice1Button.setClickable(true);
            choice2Button.setClickable(true);

            choice3Button.setVisibility(View.INVISIBLE);
            choice4Button.setVisibility(View.INVISIBLE);

            choice3Button.setClickable(false);
            choice4Button.setClickable(false);

            fillBlank.setVisibility(View.INVISIBLE);
            enterButton.setVisibility(View.INVISIBLE);

            fillBlank.setClickable(false);
            enterButton.setClickable(false);

            checkboxOption1.setVisibility(View.INVISIBLE);
            checkboxOption2.setVisibility(View.INVISIBLE);
            checkboxOption3.setVisibility(View.INVISIBLE);
            checkboxOption4.setVisibility(View.INVISIBLE);
            submitButton.setVisibility(View.INVISIBLE);

            checkboxOption1.setClickable(false);
            checkboxOption2.setClickable(false);
            checkboxOption3.setClickable(false);
            checkboxOption4.setClickable(false);
            submitButton.setClickable(false);

            // Store the correct answer
            correctAnswer = question.getBoolean("correctAnswer") ? "True" : "False";

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("PlayActivity", "Error parsing true/false question data: " + e.getMessage());
        }
    }

    /**
     * updates ui with fill in the blank questions.
     * @param question
     */
    public void updateUIWithfillBlankQuestion(JSONObject question){
        try {
            String questionText = question.getString("question");

            questionTextbox.setText(questionText);
            choice1Button.setVisibility(View.INVISIBLE);
            choice2Button.setVisibility(View.INVISIBLE);
            choice3Button.setVisibility(View.INVISIBLE);
            choice4Button.setVisibility(View.INVISIBLE);

            choice1Button.setClickable(false);
            choice2Button.setClickable(false);
            choice3Button.setClickable(false);
            choice4Button.setClickable(false);

            checkboxOption1.setVisibility(View.INVISIBLE);
            checkboxOption2.setVisibility(View.INVISIBLE);
            checkboxOption3.setVisibility(View.INVISIBLE);
            checkboxOption4.setVisibility(View.INVISIBLE);
            submitButton.setVisibility(View.INVISIBLE);

            checkboxOption1.setClickable(false);
            checkboxOption2.setClickable(false);
            checkboxOption3.setClickable(false);
            checkboxOption4.setClickable(false);
            submitButton.setClickable(false);

            fillBlank.setVisibility(View.VISIBLE);
            enterButton.setVisibility(View.VISIBLE);

            fillBlank.setClickable(true);
            enterButton.setClickable(true);

            // Store the correct answer
            correctAnswer = getCorrectAnswer(question);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("PlayActivity", "Error parsing fill in the blank question data: " + e.getMessage());
        }
    }

    /**
     * updates ui with select checkbox questions.
     * @param question
     */
    public void updateUIWithSelectQuestion(JSONObject question){

        choice1Button.setVisibility(View.INVISIBLE);
        choice2Button.setVisibility(View.INVISIBLE);
        choice3Button.setVisibility(View.INVISIBLE);
        choice4Button.setVisibility(View.INVISIBLE);

        choice1Button.setClickable(false);
        choice2Button.setClickable(false);
        choice3Button.setClickable(false);
        choice4Button.setClickable(false);

        fillBlank.setVisibility(View.INVISIBLE);
        enterButton.setVisibility(View.INVISIBLE);

        fillBlank.setClickable(false);
        enterButton.setClickable(false);

        try {
            String questionText = question.getString("question");
            String answer1 = question.getString("answer1");
            String answer1correct = question.getString("answer1correct");
            String answer2 = question.getString("answer2");
            String answer2correct = question.getString("answer2correct");
            String answer3 = question.getString("answer3");
            String answer3correct = question.getString("answer3correct");
            String answer4 = question.getString("answer4");
            String answer4correct = question.getString("answer4correct");

            questionTextbox.setText(questionText);

            checkboxOption1.setVisibility(View.VISIBLE);
            checkboxOption2.setVisibility(View.VISIBLE);
            checkboxOption3.setVisibility(View.VISIBLE);
            checkboxOption4.setVisibility(View.VISIBLE);

            checkboxOption1.setClickable(true);
            checkboxOption2.setClickable(true);
            checkboxOption3.setClickable(true);
            checkboxOption4.setClickable(true);

            checkboxOption1.setText(answer1);
            checkboxOption2.setText(answer2);
            checkboxOption3.setText(answer3);
            checkboxOption4.setText(answer4);

            submitButton.setVisibility(View.VISIBLE);
            submitButton.setClickable(true);

            correctAnswer = answer1correct + answer2correct + answer3correct + answer4correct;

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("PlayActivity", "Error parsing checkbox select question data: " + e.getMessage());
        }
    }

    /**
     * retrieves the correct answer from the question object
     * @param question - stores the question info in an object
     * @return - displays the correct answer on the screen of the application.
     * @throws JSONException
     */
    public String getCorrectAnswer(JSONObject question) throws JSONException {
        // Implement logic to extract the correct answer based on the question structure
        // Modify this based on your actual JSON structure
        return question.getString("answer1");
    }

    /**
     * takes the selectedAnswer from the OnClick listeners when a button is pressed.
     * updates logic with player stats and if incorrect ends the game.
     * @param selectedAnswer - answer button chosen
     * @throws JSONException
     */
    public void handleAnswer(String selectedAnswer) throws JSONException {

        // Convert both selectedAnswer and correctAnswer to lowercase for case-insensitive comparison
        String lowercaseSelectedAnswer = selectedAnswer.toLowerCase();
        String lowercaseCorrectAnswer = correctAnswer.toLowerCase();

        // Increment total question counter
        int newTotalQuestions = (int) playerObject.get("totalQuestions");
        newTotalQuestions += 1;

        //testing for powerUps
        if(questionsAnswered < 5 && !isGuest) {
            questionsAnswered += 1;

        }else if (questionsAnswered == 5 && !isGuest){

            givePowerup(this, playerObject.getString("playerUsername"));

            questionsAnswered = 0;
            updatePowerUpsUI();
        }

        if (lowercaseSelectedAnswer.equals(lowercaseCorrectAnswer) && !isGuest) {
            // Correct answer, proceed to the next question with incrementation
            int newTotalCorrect = (int) playerObject.get("totalCorrect") + 1;
            playerObject.put("totalCorrect", newTotalCorrect);

            int newCurrentStreak = (int) playerObject.get("currentStreak") + 1;
            playerObject.put("currentStreak", newCurrentStreak);

            // Update longest streak if needed
            if ((int) playerObject.get("longestStreak") < (int) playerObject.get("currentStreak")) {
                playerObject.put("longestStreak", (int) playerObject.get("currentStreak"));
            }

            playerObject.put("totalQuestions", newTotalQuestions);

            timeTook();

            if(paused){
                resetTimer();
                paused = false;
            }

            fetchQuestion();

        }  else if (!isGuest){
            playerObject.put("currentStreak", 0);
            playerObject.put("totalQuestions", newTotalQuestions);
            try {
                endActivity();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        if (lowercaseSelectedAnswer.equals(lowercaseCorrectAnswer) && isGuest) {

            timeTook();

            if(paused){
                resetTimer();
                paused = false;
            }

            fetchQuestion();

        } else if(isGuest){
            endActivity();
        }

        fillBlank.setText("");
    }

    /**
     * ends the activity if the wrong answer is selected or the timer runs out. Does not edit if the user is a guest
     * @throws JSONException
     */
    public void endActivity() throws JSONException {

        currentScore = 0;
        questionsAnswered = 0;
        webSocketScoreManager.disconnectScoreWebSocket();

        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;

        if(isGuest){

            if (countDownTimer != null) {
                countDownTimer.cancel();
                countDownTimer = null;
            }

            Intent intent = new Intent(PlayActivity.this, GameMenuActivity.class);
            intent.putExtra("isGuest", isGuest);
            startActivity(intent);
        }
        else {

            String url = "http://coms-309-041.class.las.iastate.edu:8080/users/";
            int id = playerObject.getInt("playerId");
            url += id;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    playerObject,

                    new Response.Listener<JSONObject>() {
                        /**
                         * Calls the function with the JSON Object as a parameter to respond for the appropriate edit
                         *
                         * @param response The Volley JSON object response from the PUT request
                         */
                        @Override
                        public void onResponse(JSONObject response) {
                            // Handle the JSON response
                            playerObject = response;

                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                                countDownTimer = null;
                            }

                            Intent intent = new Intent(PlayActivity.this, LeaderboardActivity.class);
                            intent.putExtra("playerObject", response.toString());
                            startActivity(intent);
                        }
                    },
                    new Response.ErrorListener() {
                        /**
                         * Calls the error function to display the error message for the appropriate change
                         *
                         * @param error The Volley error that caused the PUT request to fail
                         */
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle errors
                            Toast.makeText(PlayActivity.this, "Did not update score", Toast.LENGTH_SHORT).show();
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                                countDownTimer = null;
                            }
                            Intent intent = new Intent(PlayActivity.this, LeaderboardActivity.class);
                            intent.putExtra("playerObject", playerObject.toString());
                            startActivity(intent);
                        }
                    }
            );
            // Add the request to the provided Volley queue
            requestQueue.add(jsonObjectRequest);
        }
    }

    /**
     * starts timer on activity creation or on a reset.
     */
    public void startTimer() {

        countDownTimer = new CountDownTimer(COUNTDOWN_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timeLeftInMillis = millisUntilFinished;

                int progress = (int) ((millisUntilFinished / (float) COUNTDOWN_TIME) * 100);
                timerProgressBar.setProgress(progress);
            }

            @Override
            public void onFinish() {
                try {
                    endActivity();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }

    /**
     * gets the time it took for the player to answer
     */
    public void timeTook(){
        webSocketScoreManager.sendScore(String.valueOf(timeLeftInMillis));
    }

    /**
     * connects score websocket
     * @param handshakedata
     */
    @Override
    public void onWebSocketScoreOpen(ServerHandshake handshakedata) {
        Log.d("WebSocket", "Connected");
    }

    /**
     * closes the score websocket
     * @param code
     * @param reason
     * @param remote
     */
    @Override
    public void onWebSocketScoreClose(int code, String reason, boolean remote) {
        Log.d("WebSocket", "Closed");
    }

    /**
     * for error handling with the score websocket
     * @param ex - example error
     */
    @Override
    public void onWebSocketScoreError(Exception ex) {
        Log.e("WebSocket", "Error: " + ex.getMessage());
    }

    /**
     * When a player scores
     * @param score - appends to the total score.
     */
    @Override
    public void onWebSocketScore(int score) {

        if(isDouble){
            currentScore += (score * 2);
            isDouble = false;
        }

        currentScore += score;

        runOnUiThread(() ->{
           scoreBox.setText("Score:" + currentScore);
        });
    }

    /**
     * resets the timer bar when a new question is loaded, after a correct answer is selected
     */
    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Reset progress bar to max
        timerProgressBar.setProgress(timerProgressBar.getMax());

        // Start the timer again
        startTimer();
    }

    private long timeLeftInMillis;

    private boolean paused = false;

    public void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
            paused = true;
        }
    }

    /**
     * closes the timer bar and stops music when the app is destroyed to prevent memory leaks.
     */
    @Override
    protected void onDestroy() {

        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer=null;
        }
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if(webSocketScoreManager != null){
            webSocketScoreManager.disconnectScoreWebSocket();
        }
        super.onDestroy();
    }

    /** POWERUPS RELATED SECTION */

    public String url1 = "http://coms-309-041.class.las.iastate.edu:8080/powerup/";

    public String url = "http://coms-309-041.class.las.iastate.edu:8080/powerups/";

    /**
     * String value of double points powerups
     */
    public String timesTwoCount;

    /**
     * String value of freeze powerups
     */
    public String freezeCount;

    /**
     * String value of troll powerups
     */
    public String trollCount;

    /**
     * integer count of freeze powerups
     */
    public int fcount;

    /**
     * integer count of double points powerups
     */
    public int dcount;

    /**
     * integer count of troll powerups
     */
    public int tcount;

    /**
     * updates powerup views
     */
    public void updatePowerUpsUI(){
            try {
                getUserPowerups(PlayActivity.this, playerObject.getString("playerUsername"));

                doublePointsText.setText(timesTwoCount);
                freezeText.setText(freezeCount);
                trollText.setText(trollCount);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
    }

    /**
     * sends server the amount of each powerup the user already has
     *
     * @param username
     * @return
     */
    public void getUserPowerups(Context context, String username){

        String listen = url + username;

        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, listen,
                new Response.Listener<String>() {
                    /**
                     * sends a message saying the question saved correctly over volley.
                     * @param response - text displayed on screen.
                     */
                    @Override
                    public void onResponse(String response) {
                        try {
                            String[] splitResponse = response.split("%3A");
                            if (splitResponse.length >= 3) {
                                timesTwoCount = String.valueOf(Integer.parseInt(splitResponse[0]));
                                freezeCount = String.valueOf(Integer.parseInt(splitResponse[1]));
                                trollCount = String.valueOf(Integer.parseInt(splitResponse[2]));

                                dcount = Integer.parseInt(timesTwoCount);
                                fcount = Integer.parseInt(freezeCount);
                                tcount = Integer.parseInt(trollCount);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * sends an error message if the question doesn't save or send over volley.
                     * @param error - error message text
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                }
        ) {
        };

        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }

    /**
     * for user that has never gotten powerups, gives a random one to create a table
     * otherwise gives a powerup
     * @param username - user playing game
     */
    public void givePowerup(Context context, String username){
        String listen = url1 + "new" + "/" + username;

        // Make a POST request using Volley to send the question data to the server
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT, listen,
                new Response.Listener<String>() {
                    /**
                     * sends a message saying the question saved correctly over volley.
                     * @param response - text displayed on screen.
                     */
                    @Override
                    public void onResponse(String response){
                        Toast.makeText(PlayActivity.this, "Powerup Received", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * sends an error message if the question doesn't save or send over volley.
                     * @param error - error message text
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                }
        ) {};

        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }

    /**
     * when a user plays this powerup it removes 1 from its count
     * @param username
     */
    public void usefreezePowerup(Context context, String username){

        String listen = url + "freeze" + "/" + username;

        // Make a POST request using Volley to send the question data to the server
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT, listen,
                new Response.Listener<String>() {
                    /**
                     * sends a message saying the question saved correctly over volley.
                     * @param response - text displayed on screen.
                     */
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(PlayActivity.this, "FREEZE", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * sends an error message if the question doesn't save or send over volley.
                     * @param error - error message text
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                }
        ) {
        };

        // Add the request to the RequestQueue
        queue.add(stringRequest);

    }

    /**
     * when a user plays this powerup it removes 1 from its count
     * @param username
     */
    public void usetwoTimesPowerup(Context context, String username){

        String listen = url + "double" + "/" + username;

        // Make a POST request using Volley to send the question data to the server
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT, listen,
                new Response.Listener<String>() {
                    /**
                     * sends a message saying the question saved correctly over volley.
                     * @param response - text displayed on screen.
                     */
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(PlayActivity.this, "HERE COMES DA MONEY", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * sends an error message if the question doesn't save or send over volley.
                     * @param error - error message text
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                }
        ) {
        };

        // Add the request to the RequestQueue
        queue.add(stringRequest);

    }

    /**
     * when a user plays this powerup it removes 1 from its count
     * @param username
     */
    public void usetrollPowerup(Context context, String username){

        String listen = url + "troll" + "/" + username;

        // Make a POST request using Volley to send the question data to the server
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT, listen,
                new Response.Listener<String>() {
                    /**
                     * sends a message saying the question saved correctly over volley.
                     * @param response - text displayed on screen.
                     */
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(PlayActivity.this, "LOL U BAD", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * sends an error message if the question doesn't save or send over volley.
                     * @param error - error message text
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                }
        ) {
        };
        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }
}