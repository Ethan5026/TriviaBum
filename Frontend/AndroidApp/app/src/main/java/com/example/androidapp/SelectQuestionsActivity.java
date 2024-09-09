package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that implements an activity that is used to control the creation/deletion/editing of questions through the app.
 */
public class SelectQuestionsActivity extends AppCompatActivity {

    /**
     * edits the question being asked on screen.
     */
    private EditText editTextQuestion;

    /**
     * selects the question to delete based on the id entered.
     */
    private EditText deleteQuestionId;

    /**
     * edits the text of the correct answer.
     */
    private EditText editTextCorrect;

    /**
     * edits the second displayed answer on screen.
     */
    private EditText editTextAnswer2;

    /**
     * edits the third answer on screen.
     */
    private EditText editTextAnswer3;

    /**
     * edits the fourth answer on screen.
     */
    private EditText editTextAnswer4;

    /**
     * id of question to be edited.
     */
    private EditText editTextEDITid;

    /**
     * button to send the question edited.
     */
    private Button editButton;

    /**
     * sends new question to be created.
     */
    private Button buttonSave;

    /**
     * sends the question to be deleted.
     */
    private Button buttonDelete;

    /**
     * backs out of the activity.
     */
    private Button backButton;

    private CompoundButton checkBox1;
    private CompoundButton checkBox2;
    private CompoundButton checkBox3;
    private CompoundButton checkBox4;

    public boolean isChecked1;
    public boolean isChecked2;
    public boolean isChecked3;
    public boolean isChecked4;
    /**
     * Holds the JSON object for the player including its information
     */
    private JSONObject playerObject;

    public String check1, check2, check3, check4;

    /**
     * creates the activity and loads the xml view.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectquestions);

        // Initialize UI elements
        editTextQuestion = findViewById(R.id.editTextQuestion);
        editTextCorrect = findViewById(R.id.editTextCorrect);
        editTextAnswer2 = findViewById(R.id.editTextIncorrect1);
        editTextAnswer3 = findViewById(R.id.editTextIncorrect2);
        editTextAnswer4 = findViewById(R.id.editTextIncorrect3);
        deleteQuestionId = findViewById(R.id.deleteId);
        editTextEDITid = findViewById(R.id.editquestionid);

        checkBox1 = findViewById(R.id.checkboxOption1);
        checkBox2 = findViewById(R.id.checkboxOption2);
        checkBox3 = findViewById(R.id.checkboxOption3);
        checkBox4 = findViewById(R.id.checkboxOption4);

        editButton = findViewById(R.id.editButton);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);
        backButton = findViewById(R.id.backButton);

        isChecked1 = false;
        isChecked2 = false;
        isChecked3 = false;
        isChecked4 = false;

        checkBox1.setChecked(false);
        checkBox2.setChecked(false);
        checkBox3.setChecked(false);
        checkBox4.setChecked(false);

        try {
            playerObject = new JSONObject(getIntent().getStringExtra("playerObject"));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            /**
             * sends the entered question to be created in the database.
             */
            @Override
            public void onClick(View view) {

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
                saveQuestion();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            /**
             * when clicked sends the entered question by its id to be removed from the database.
             */
            @Override
            public void onClick(View view) {
                deleteQuestion();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            /**
             * when clicked sends the edited question to the database.
             */
            @Override
            public void onClick(View view) {

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

                editQuestion();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * loads the previous activity
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectQuestionsActivity.this, QuestionCreatorActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                startActivity(intent);
            }
        });

        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked1){
                    isChecked1 = false;
                }else{
                    isChecked1 = true;
                }
            }
        });

        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked2){
                    isChecked2 = false;
                }else{
                    isChecked2 = true;
                }
            }
        });

        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked3){
                    isChecked3 = false;
                }else{
                    isChecked3 = true;
                }
            }
        });

        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChecked4){
                    isChecked4 = false;
                }else{
                    isChecked4 = true;
                }
            }
        });

    }

    /**
     * loads the entered text from the entry boxes to be sent to the database as a new question.
     */
    private void saveQuestion() {
        String answer1 = editTextCorrect.getText().toString();
        String answer1correct = check1;
        String answer2 = editTextAnswer2.getText().toString();
        String answer2correct = check2;
        String answer3 = editTextAnswer3.getText().toString();
        String answer3correct = check3;
        String answer4 = editTextAnswer4.getText().toString();
        String answer4correct = check4;
        String question = editTextQuestion.getText().toString();

        String requestBody = answer1 + "%3A" + answer1correct + "%3A"+ answer2 + "%3A" + answer2correct + "%3A" + answer3 + "%3A" + answer3correct + "%3A" + answer4 + "%3A" + answer4correct + "%3A" + question;

        // Specify the URL of your backend API for creating a multiple-choice question
        String apiUrl = "http://coms-309-041.class.las.iastate.edu:8080/select/create";

        // Create a JSON object with the question data
        JSONObject questionObject = new JSONObject();
        try {
            questionObject.put("answer1", answer1);
            questionObject.put("answer1correct", answer1correct);
            questionObject.put("answer2", answer2);
            questionObject.put("answer2correct", answer2correct);
            questionObject.put("answer3", answer3);
            questionObject.put("answer3correct", answer3correct);
            questionObject.put("answer4", answer4);
            questionObject.put("answer4correct", answer4correct);
            questionObject.put("question", question);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make a POST request using Volley to send the question data to the server
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, apiUrl,
                new Response.Listener<String>() {
                    /**
                     * sends a message saying the question saved correctly over volley.
                     * @param response - text displayed on screen.
                     */
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SelectQuestionsActivity.this, "Question saved successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * sends an error message if the question doesn't save or send over volley.
                     * @param error - error message text
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SelectQuestionsActivity.this, "Error saving question", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        ) {
            /**
             * gets the request body by Bytes.
             * @return - returns the bytes of data in request body.
             */
            @Override
            public byte[] getBody() {
                return requestBody.getBytes();
            }

            /**
             * retrieves the body content type.
             * @return - body type data.
             */
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };

        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }

    /**
     * loads the entered question data to edit an existing question in the database by its id.
     */
    private void editQuestion() {
        // Get the text from the EditText views
        String id = editTextEDITid.getText().toString();
        String answer1 = editTextCorrect.getText().toString();
        String answer1correct = check1;
        String answer2 = editTextAnswer2.getText().toString();
        String answer2correct = check2;
        String answer3 = editTextAnswer3.getText().toString();
        String answer3correct = check3;
        String answer4 = editTextAnswer4.getText().toString();
        String answer4correct = check4;
        String question = editTextQuestion.getText().toString();

        String requestBody = id + "%3A" + answer1 + "%3A" + answer1correct + "%3A"+ answer2 + "%3A" + answer2correct + "%3A" + answer3 + "%3A" + answer3correct + "%3A" + answer4 + "%3A" + answer4correct + "%3A" + question;

        // Specify the URL of your backend API for creating a multiple-choice question
        String apiUrl = "http://coms-309-041.class.las.iastate.edu:8080/select/edit";

        // Create a JSON object with the question data
        JSONObject questionObject = new JSONObject();
        try {

            questionObject.put("id", id);
            questionObject.put("answer1", answer1);
            questionObject.put("answer1correct", answer1correct);
            questionObject.put("answer2", answer2);
            questionObject.put("answer2correct", answer2correct);
            questionObject.put("answer3", answer3);
            questionObject.put("answer3correct", answer3correct);
            questionObject.put("answer4", answer4);
            questionObject.put("answer4correct", answer4correct);
            questionObject.put("question", question);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Make a POST request using Volley to send the question data to the server
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT, apiUrl,
                new Response.Listener<String>() {
                    /**
                     * sends a message saying the question was saved correctly.
                     * @param response - text displayed on screen.
                     */
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SelectQuestionsActivity.this, "Question saved successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * sends an error message when the question text might have an issue or if volley doesn't work.
                     * @param error - text displayed on screen.
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SelectQuestionsActivity.this, "Error saving question", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        ) {
            /**
             * gets the Bytes of data from the request body.
             * @return - bytes of request body data.
             */
            @Override
            public byte[] getBody() {
                return requestBody.getBytes();
            }

            /**
             * retrieves the body content type.
             * @return - type of content.
             */
            @Override
            public String getBodyContentType() {
                return "application/java";
            }
        };

        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }

    /**
     * deletes the loaded question by the given Id in the text entry box.
     */
    private void deleteQuestion() {
        // Retrieve the text from the EditText for question ID
        String questionIdText = deleteQuestionId.getText().toString();

        // Check if the entered ID is not empty
        if (!questionIdText.isEmpty()) {
            int questionId = Integer.parseInt(questionIdText);

            // Specify the URL of your backend API for deleting a multiple-choice question
            String apiUrl = "http://coms-309-041.class.las.iastate.edu:8080/select/" + questionId;

            // Make a DELETE request using Volley to delete the question
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(
                    Request.Method.DELETE, apiUrl,
                    new Response.Listener<String>() {
                        /**
                         * sends a message when the question is succesfully deleted
                         * @param response - text displayed on screen.
                         */
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(SelectQuestionsActivity.this, "Question deleted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        /**
                         * sends error message when the question cant be found by Id or volley fails.
                         * @param error - text displayed on screen.
                         */
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SelectQuestionsActivity.this, "Error deleting question", Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }
            );

            // Add the request to the RequestQueue
            queue.add(stringRequest);
        } else {
            Toast.makeText(this, "Please enter a valid Question ID", Toast.LENGTH_SHORT).show();
        }
    }
}
