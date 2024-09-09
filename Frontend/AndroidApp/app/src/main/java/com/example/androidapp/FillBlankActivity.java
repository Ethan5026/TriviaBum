package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
public class FillBlankActivity extends AppCompatActivity {

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

    /**
     * Holds the JSON object for the player including its information
     */
    private JSONObject playerObject;

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
        setContentView(R.layout.activity_fillblankcreator);

        // Initialize UI elements
        editTextQuestion = findViewById(R.id.editTextQuestion);
        editTextCorrect = findViewById(R.id.editTextCorrect);
        deleteQuestionId = findViewById(R.id.deleteId);
        editTextEDITid = findViewById(R.id.editquestionid);
        editButton = findViewById(R.id.editButton);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);
        backButton = findViewById(R.id.backButton);
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
                Intent intent = new Intent(FillBlankActivity.this, QuestionCreatorActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                startActivity(intent);
            }
        });
    }

    /**
     * loads the entered text from the entry boxes to be sent to the database as a new question.
     */
    private void saveQuestion() {
        // Get the text from the EditText views
        String answer1 = editTextCorrect.getText().toString();
        String question = editTextQuestion.getText().toString();

        String requestBody = answer1 + "%3A" + question;

        // Specify the URL of your backend API for creating a multiple-choice question
        String apiUrl = "http://coms-309-041.class.las.iastate.edu:8080/fillblank/create";

        // Create a JSON object with the question data
        JSONObject questionObject = new JSONObject();
        try {
            questionObject.put("answer1", answer1);
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
                        Toast.makeText(FillBlankActivity.this, "Question saved successfully", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(FillBlankActivity.this, "Error saving question", Toast.LENGTH_SHORT).show();
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
        String question = editTextQuestion.getText().toString();

        String requestBody = id + "%3A" + answer1 + "%3A" + question;

        // Specify the URL of your backend API for creating a multiple-choice question
        String apiUrl = "http://coms-309-041.class.las.iastate.edu:8080/fillblank/edit";

        // Create a JSON object with the question data
        JSONObject questionObject = new JSONObject();
        try {

            questionObject.put("id", id);
            questionObject.put("answer1", answer1);
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
                        Toast.makeText(FillBlankActivity.this, "Question saved successfully", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(FillBlankActivity.this, "Error saving question", Toast.LENGTH_SHORT).show();
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
            String apiUrl = "http://coms-309-041.class.las.iastate.edu:8080/fillblank/" + questionId;

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
                            Toast.makeText(FillBlankActivity.this, "Question deleted successfully", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(FillBlankActivity.this, "Error deleting question", Toast.LENGTH_SHORT).show();
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
