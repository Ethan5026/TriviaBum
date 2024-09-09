package com.example.androidapp;//Types of widgets
import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidapp.LoginSignupActivity;



import org.json.JSONException;
import org.json.JSONObject;


import androidx.appcompat.app.AppCompatActivity;

/**
 * The class linking xlm elements to display a user's stats using
 * the user's object.
 */
public class StatsActivity extends AppCompatActivity{

    /**
     * The JSON object of a user, containing its stats information.
     */
    private JSONObject playerObject;

    /**
     * The JSON object of the user to display its stats information.
     */
    private JSONObject statsObject;

    /**
     * The request queue for volley get request
     */
    private RequestQueue requestQueue;
    /**
     * String array holding the list of usernames of the player's friends
     */
    private String[] friendsArray;
    /**
     * The spinner object for the friends to be listed
     */
    private Spinner spinner;

    /**
     * int holding the position currently selected by the spinner
     */
    private int spinner_position = 0;
    /**
     * Where to route to when pressing the back button. 0 = GameMenu, 1 = Clan Page
     */
    private int backRouter;
    /**
     * Holds the error or success messages during volley requests
     */
    private TextView errorBox;

    /**
     * The Volley request queue to help perform CRUDL operations
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        uiDesign.applyRandomBackgroundColor(this);


        // Retrieve the JSONObject from the Intent
        Intent intent = getIntent();
        String title = null;
        String currStreak = null;
        String longStreak = null;
        String totQuestions = null;
        String totCorr = null;
        String corrRatio = null;
        String username = null;
        requestQueue = Volley.newRequestQueue(this);
        errorBox = (TextView)findViewById(R.id.errorBox);

        if (intent.hasExtra("playerObject")) {
            String jsonString = intent.getStringExtra("playerObject");
            String jsonString2 = intent.getStringExtra("statsObject");
            backRouter = intent.getIntExtra("routeTo", 0);

            // Convert the String back to JSONObject
            try {
                assert jsonString != null;
                playerObject = new JSONObject(jsonString);
                statsObject = new JSONObject(jsonString2);
                username = playerObject.getString("playerUsername");
                String statsUsername = statsObject.getString("playerUsername");
                getPlayer(statsUsername);
                if(username.charAt(username.length() - 1) == 's'){
                    title = statsUsername + "' Statistics";
                }
                else {
                    title = statsUsername + "'s Statistics";
                }
                currStreak = "Current Streak: " + statsObject.getInt("currentStreak");
                longStreak = "Longest Streak: " + statsObject.getInt("longestStreak");
                totQuestions="Total Questions: " + statsObject.getInt("totalQuestions");
                totCorr =    "Total Correct: " + statsObject.getInt("totalCorrect");
                corrRatio =  "Correct Ratio: " + statsObject.getDouble("correctRatio");
                errorBox.setText("Retrieved Stats Successfully");
            } catch (JSONException e) {
                errorBox.setText("Unable to retrieve stats");
            }
        }
        Button backButton = (Button) findViewById(R.id.backButton);
        Button myStats = (Button) findViewById(R.id.myStats);
        Button friendButton = (Button)findViewById(R.id.friendButton);
        ((TextView)findViewById(R.id.statsTitle)).setText(title);
        ((TextView)findViewById(R.id.currentStreak)).setText(currStreak);
        ((TextView)findViewById(R.id.longestStreak)).setText(longStreak);
        ((TextView)findViewById(R.id.totalQuestions)).setText(totQuestions);
        ((TextView)findViewById(R.id.totalCorrect)).setText(totCorr);
        ((TextView)findViewById(R.id.correctRatio)).setText(corrRatio);
        spinner = (Spinner)findViewById(R.id.friendSpinner);
        String apiUrl = "http://coms-309-041.class.las.iastate.edu:8080/friends/" + username;

        // Make a GET request using Volley to fetch the list of friends
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, apiUrl,
                new Response.Listener<String>() {
                    /**
                     * Handle the response containing the string of friends.
                     * @param response The string response containing the list of friends.
                     */
                    @Override
                    public void onResponse(String response) {
                        // Split the string response into an array of friend usernames
                        friendsArray = response.split(" ");
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(StatsActivity.this, android.R.layout.simple_spinner_item, friendsArray);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner.setAdapter(adapter);
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * Handle errors that occur during the GET request.
                     * @param error The error object containing details about the error.
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorBox.setText("Unable to retrieve friend's stats");
                    }
                }
        );

        // Add the request to the RequestQueue
        requestQueue.add(stringRequest);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * dropdown for selecting a friend to view their stats
             *
             * @param parentView The AdapterView where the selection happened
             * @param selectedItemView The view within the AdapterView that was clicked
             * @param position The position of the view in the adapter
             * @param id The row id of the item that is selected
             */
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinner_position = position;
            }

            /**
             * does nothing when nothing is selected from the dropdown.
             * @param parentView The AdapterView that now contains no selected item.
             */
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Sends the user back to the Game Menu with the player object
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if(backRouter == 0) {
                    intent = new Intent(StatsActivity.this, GameMenuActivity.class);
                }
                else{
                    intent = new Intent(StatsActivity.this, ClansActivity.class);
                }
                intent.putExtra("playerObject", playerObject.toString());
                startActivity(intent);
            }
        });
        friendButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Sends the user back to the Game Menu with the player object
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                getFriendsStats(friendsArray[spinner_position]);
            }
        });
        myStats.setOnClickListener(new View.OnClickListener() {
            /**
             * Refreshes the Stats page using the player's info as the stats
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatsActivity.this, StatsActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                intent.putExtra("statsObject", playerObject.toString());
                startActivity(intent);
            }
        });
    }
    private void getPlayer(String username){
        String url = "http://coms-309-041.class.las.iastate.edu:8080/users/" + username;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    /**
                     * Sets the instance variable of the playerObject on a valid Volley response
                     * @param response
                     */
                    @Override
                    public void onResponse(JSONObject response) {
                        statsObject = response;
                    }
                },
                /**
                 * Displays an error that the player could not be updated
                 */
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorBox.setText("Unable to retrieve user's data");
                    }
                }
                );
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Creates and executes a volley request to update the stats page and view a friend's stats
     * @param username the username of the friend they would like to view
     */
    private void getFriendsStats(String username){
        String url = "http://coms-309-041.class.las.iastate.edu:8080/users/" + username;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    /**
                     * Sets the instance variable of the playerObject on a valid Volley response
                     * @param response
                     */
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(StatsActivity.this, StatsActivity.class);
                        intent.putExtra("statsObject", response.toString());
                        intent.putExtra("playerObject", playerObject.toString());
                        startActivity(intent);
                    }
                },
                /**
                 * Displays an error that the player could not be updated
                 */
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorBox.setText("Unable to retrieve user's stats");
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}
