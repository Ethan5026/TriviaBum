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
public class QuestionCreatorActivity extends AppCompatActivity {

    /**
     * backs out of the activity.
     */
    private Button backButton;

    /**
     * player info object
     */
    JSONObject playerObject;

    // Spinner object
    private Spinner genreSpinner;

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
        setContentView(R.layout.activity_question_creator);

        Intent intent = getIntent();
        if (intent.hasExtra("playerObject")) {
            String jsonString = intent.getStringExtra("playerObject");

            // Convert the String back to JSONObject
            assert jsonString != null;
            try {
                playerObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        // Initialize UI elements
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * loads the previous activity
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionCreatorActivity.this, GameMenuActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                startActivity(intent);
            }
        });

        // Initialize spinner
        genreSpinner = findViewById(R.id.typeSpinner);
        ArrayAdapter<CharSequence> genreAdapter = ArrayAdapter.createFromResource(
                this, R.array.genre_array, android.R.layout.simple_spinner_item
        );
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreSpinner.setAdapter(genreAdapter);

        genreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()

        {
            /**
             * dropdown for selecting question type.
             *
             * @param parentView       The AdapterView where the selection happened
             * @param selectedItemView The view within the AdapterView that was clicked
             * @param position         The position of the view in the adapter
             * @param id               The row id of the item that is selected
             */
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                       int position, long id) {

                String selectedGenre = parentView.getItemAtPosition(position).toString();

                Intent intent = null;

                switch (selectedGenre) {
                    case "Choose Option":
                        intent = null;
                        break;
                    case "Multiple Choice":
                        intent = new Intent(QuestionCreatorActivity.this, MultipleChoiceActivity.class);
                        break;
                    case "True or False":
                        intent = new Intent(QuestionCreatorActivity.this, TrueFalseActivity.class);
                        break;
                    case "Fill in the Blank":
                        intent = new Intent(QuestionCreatorActivity.this, FillBlankActivity.class);
                        break;
                    case "Check Box":
                        intent = new Intent(QuestionCreatorActivity.this, SelectQuestionsActivity.class);
                        break;
                    default:
                        intent = null;
                        break;
                }

                if (intent != null) {
                    intent.putExtra("playerObject", playerObject.toString());
                    startActivity(intent);
                }
            }

            /**
             * does nothing when nothing is selected from the dropdown.
             * @param parentView The AdapterView that now contains no selected item.
             */
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here if nothing is selected
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Clear spinner selection
        if (genreSpinner != null) {
            genreSpinner.setSelection(0);
        }
    }
}
