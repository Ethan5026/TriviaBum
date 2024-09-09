package com.example.androidapp;//Types of widgets
import android.accounts.Account;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Controller for the Leaderboard screen to display the top 5 scoring
 * players on the leaderboard by using xml object design.
 */
public class LeaderboardActivity extends AppCompatActivity{
    /**
     * The JSON object of the user, holding its information
     */
    private JSONObject playerObject;
    /**
     * The JSON Array that is set with the top 5 scoring players
     */
    private static JSONArray topPlayers;
    /**
     * The Volley request queue to perform the CRUDL operations
     */
    private static RequestQueue requestQueue;
    /**
     * The error text box to display any Volley errors
     */
    private static TextView errorBox;

    /**
     * The MediaPlayer to produce sound effects
     */
    private MediaPlayer mediaPlayer;


    /**
     * Method to create and bind xml design units to the Leaderboard screen
     * to view, press, and set display.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        uiDesign.applyRandomBackgroundColor(this);

        requestQueue = Volley.newRequestQueue(this);
        Button backButton = (Button) findViewById(R.id.backButton);
        Button orgsButton = (Button) findViewById(R.id.orgsButton);
        Button myOrgButton = (Button) findViewById(R.id.myOrgButton);
        Button allPlayersButton = (Button) findViewById(R.id.allPlayersButton);
        errorBox = (TextView) findViewById(R.id.errorBox);
        Button friendsButton = (Button) findViewById(R.id.myFriendsButton);

        // Retrieve the JSONObject from the Intent
        Intent intent = getIntent();
        if (intent.hasExtra("playerObject")) {
            String jsonString = intent.getStringExtra("playerObject");

            // Convert the String back to JSONObject
            try {
                assert jsonString != null;
                playerObject = new JSONObject(jsonString);

                // Now you can use the receivedJsonObject as needed

            } catch (JSONException e) {
                errorBox.setText("Could not load leaderboard");
            }
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Sends the user back to the GameMenu page with their info
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;

                Intent intent = new Intent(LeaderboardActivity.this, GameMenuActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                startActivity(intent);
            }

        });
        allPlayersButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Sends the user back to the GameMenu page with their info
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                getTopPlayers();
            }
        });

        friendsButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Updates the leaderboard to display the top scoring players of the user and their friends
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                try {
                    getTopFriends(playerObject.getString("playerUsername"));
                } catch (JSONException e) {
                    errorBox.setText("Could not load leaderboard");
                }
            }
        });
        orgsButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Displays the top clans in the leaderboard when clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                getTopOrgs();
            }
        });
        myOrgButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Display the top players in the user's clan in the leaderboard when clicked
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {

                try {
                    getTopInOrg(playerObject.getString("playerUsername"));
                } catch (JSONException e) {
                    errorBox.setText("Could not load leaderboard");
                };
            }
        });
        //default by getting the top players out of total pool of players
        getTopPlayers();
        mediaPlayer = MediaPlayer.create(LeaderboardActivity.this, R.raw.crowd_cheer);
        mediaPlayer.start();
    }

    /**
     * Places a list of data from the topPlayer's instance variable into the leaderboard display
     */
    public void placeInData(boolean players){

        //fill the leaderboard with the players and their stats
        ArrayList<String> leaderboardContents = new ArrayList<>();

        if(players){
            for(int i = 0; i < 5; i++) {
                try {
                    if (topPlayers.getJSONObject(i) == null) {
                        leaderboardContents.add(i, "Not enough data");
                    } else {
                        JSONObject player = topPlayers.getJSONObject(i);
                        String message = "#" + (i + 1) + ": " + player.getString("playerUsername") + "\n" + "Correct Ratio: " + player.getDouble("correctRatio");
                        leaderboardContents.add(i, message);
                    }
                } catch (JSONException e) {
                    errorBox.setText("Could not load leaderboard");
                }
            }
            ((TextView)findViewById(R.id.player1)).setText("Not Enough Data");
            ((TextView)findViewById(R.id.player2)).setText("Not Enough Data");
            ((TextView)findViewById(R.id.player3)).setText("Not Enough Data");
            ((TextView)findViewById(R.id.player4)).setText("Not Enough Data");
            ((TextView)findViewById(R.id.player5)).setText("Not Enough Data");
            ((TextView)findViewById(R.id.player1)).setText(leaderboardContents.get(0));
            ((TextView)findViewById(R.id.player2)).setText(leaderboardContents.get(1));
            ((TextView)findViewById(R.id.player3)).setText(leaderboardContents.get(2));
            ((TextView)findViewById(R.id.player4)).setText(leaderboardContents.get(3));
            ((TextView)findViewById(R.id.player5)).setText(leaderboardContents.get(4));
        }
        else{
            for(int i = 0; i < 5; i++) {
                try {
                    if (topPlayers.getJSONObject(i) == null) {
                        leaderboardContents.add(i, "Not enough data");
                    } else {
                        JSONObject org = topPlayers.getJSONObject(i);
                        String message = "#" + (i+1) + "\n" + org.getString("orgName");
                        leaderboardContents.add(i, message);
                    }
                } catch (JSONException e) {
                    errorBox.setText("Could not load leaderboard");
                }
            }
            ((TextView)findViewById(R.id.player1)).setText("Not Enough Data");
            ((TextView)findViewById(R.id.player2)).setText("Not Enough Data");
            ((TextView)findViewById(R.id.player3)).setText("Not Enough Data");
            ((TextView)findViewById(R.id.player4)).setText("Not Enough Data");
            ((TextView)findViewById(R.id.player5)).setText("Not Enough Data");
            ((TextView)findViewById(R.id.player1)).setText(leaderboardContents.get(0));
            ((TextView)findViewById(R.id.player2)).setText(leaderboardContents.get(1));
            ((TextView)findViewById(R.id.player3)).setText(leaderboardContents.get(2));
            ((TextView)findViewById(R.id.player4)).setText(leaderboardContents.get(3));
            ((TextView)findViewById(R.id.player5)).setText(leaderboardContents.get(4));
        }
    }

    /**
     * The Get request to get a JSON Array of the top scoring 5 players and set them to topPlayers
     */
    private void getTopPlayers(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://coms-309-041.class.las.iastate.edu:8080/leaderboard/top",
                null,
                new Response.Listener<JSONArray>() {
                    /**
                     * On a successful GET request, the topPlayers instance variable is set to the response
                     * @param response The JSONArray object from the Volley GET request
                     */
                    @Override
                    public void onResponse(JSONArray response) {
                        topPlayers = response;
                        errorBox.setText("Leaderboard Updated: All Players");
                        placeInData(true);
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * On an unsuccessful GET request, the error message is displayed
                     * @param error THe Volley error causing the request to fail
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        errorBox.setText("Could not load leaderboard, null values");
                        topPlayers = null;
                    }
                }


        );
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Gets the top organizations to display onto the leaderboard
     */
    private void getTopOrgs(){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://coms-309-041.class.las.iastate.edu:8080/leaderboard/top/orgs",
                null,
                new Response.Listener<JSONArray>() {
                    /**
                     * On a successful GET request, the topPlayers instance variable is set to the response
                     * @param response The JSONArray object from the Volley GET request
                     */
                    @Override
                    public void onResponse(JSONArray response) {
                        topPlayers = response;
                        try {
                            errorBox.setText("Leaderboard Updated: Top Clans");
                            placeInData(false);
                        }
                        catch(Exception e){
                            errorBox.setText("Could not load leaderboard");
                        }
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * On an unsuccessful GET request, the error message is displayed
                     * @param error THe Volley error causing the request to fail
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        errorBox.setText("Could not load leaderboard");
                        topPlayers = null;
                    }
                }


        );
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Gets the top 5 players within an organization
     */
    private void getTopInOrg(String username){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://coms-309-041.class.las.iastate.edu:8080/leaderboard/top/org/" + username,
                null,
                new Response.Listener<JSONArray>() {
                    /**
                     * On a successful GET request, the topPlayers instance variable is set to the response
                     * @param response The JSONArray object from the Volley GET request
                     */
                    @Override
                    public void onResponse(JSONArray response) {
                        topPlayers = response;
                        errorBox.setText("Leaderboard Updated: My Clan");
                        placeInData(true);
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * On an unsuccessful GET request, the error message is displayed
                     * @param error THe Volley error causing the request to fail
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        errorBox.setText("Could not load leaderboard");
                        topPlayers = null;
                    }
                }


        );
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * The Get request to get a JSON Array of the top scoring 5 players of a user's friends and themself
     */
    private void getTopFriends(String username) throws JSONException {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://coms-309-041.class.las.iastate.edu:8080/leaderboard/top/friends/" + username,
                null,
                new Response.Listener<JSONArray>() {
                    /**
                     * On a successful GET request, the topPlayers instance variable is set to the response
                     * @param response The JSONArray object from the Volley GET request
                     */
                    @Override
                    public void onResponse(JSONArray response) {
                        topPlayers = response;
                        errorBox.setText("Leaderboard Updated: My Friends");
                        placeInData(true);
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * On an unsuccessful GET request, the error message is displayed
                     * @param error THe Volley error causing the request to fail
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        errorBox.setText("Could not load leaderboard");
                        topPlayers = null;
                    }
                }


        );
        requestQueue.add(jsonArrayRequest);
    }


    /**
     * Stops the music from playing
     */
    @Override
    public void onDestroy() {
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

        }
        super.onDestroy();
    }
}
