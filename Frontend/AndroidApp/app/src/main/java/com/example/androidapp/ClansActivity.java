package com.example.androidapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * This class operates the clans page where you can message and add friends from members within the clan
 */
public class ClansActivity extends AppCompatActivity implements WebSocketListener{

    /**
     * Holds the value of the JSON user object and their info
     */
    private JSONObject playerObject;
    /**
     * Volley requeust queue to use to make volley requests for CRUDL operations
     */
    private RequestQueue requestQueue;

    /**
     * The text entry to send the message through the websocket chat connection to the clan
     */
    private EditText chatEntry;

    /**
     * Holds the list of usernames of all users in the clan
     */
    private static String[] clanList;
    /**
     * The dropdown to hold all the members of the clan to add them as a friend or view their stats
     */
    private Spinner spinner;

    /**
     * The currently selected username from the clan usernamne spinner
     */
    private int spinner_position = 0;

    /**
     * Holds the string of the user's clan
     */
    private String orgName;
    /**
     * Holds the integer of the organization's ID
     */
    private int orgId;
    /**
     * Holds the string of the user's username
     */
    private String username;
    /**
     * The textview for the websocket connected clan chat
     */
    private TextView txChat;
    /**
     * The ScrollView containing the textview for the websocket connected clan chat
     */
    private ScrollView scrollView;

    /**
     * loads the websocket manager.
     */
    private WebSocketManager webSocketManager;

    /**
     * Holds the string name of the user's clan
     */
    private String clanName;

    /**
     * The TextView element of the error message for volley responses.
     */
    private TextView errorText;


    /**
     * Binds the back button to route back to the MainActivity page
     * on creation of the page.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clans);

        uiDesign.applyRandomBackgroundColor(this);


        Intent intent = getIntent();
        requestQueue = Volley.newRequestQueue(this);
        if (intent.hasExtra("playerObject")) {
            String jsonString = intent.getStringExtra("playerObject");


            // Convert the String back to JSONObject
            try {
                assert jsonString != null;
                playerObject = new JSONObject(jsonString);
                username = playerObject.getString("playerUsername");

            } catch (JSONException e) {
                errorText.setText("Could not load Clan Page. Exiting");
                intent = new Intent(ClansActivity.this, GameMenuActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                startActivity(intent);
            }
        }


        Button backButton = findViewById(R.id.back_button);
        Button sendButton = findViewById(R.id.sendButton);
        Button friendButton = findViewById(R.id.friendButton);
        Button statsButton = findViewById(R.id.friendStatsButton);
        Button profileButton = findViewById(R.id.profileButton);
        spinner = findViewById(R.id.usernameSpinner);
        chatEntry = findViewById(R.id.chatTextEntry);
        txChat = (TextView)findViewById(R.id.chatText);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        errorText = (TextView)findViewById(R.id.errorText);


        try{
            getOrg(username);

        }
        catch(Error e){
            errorText.setText("Could not load Clan chat. Exiting");
            intent = new Intent(ClansActivity.this, GameMenuActivity.class);
            intent.putExtra("playerObject", playerObject.toString());
            startActivity(intent);
        }


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * dropdown for selecting the user of a clan.
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
        profileButton.setOnClickListener(new View.OnClickListener() {
            /**
             * loads the profile of a clan member
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                onWebSocketClose(1, "Exiting Clan page", true);
                Intent intent = new Intent(ClansActivity.this, ProfileActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                intent.putExtra("profileUsername", clanList[spinner_position]);
                intent.putExtra("routeTo", 1);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Sends the user back to the Main Activity
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClansActivity.this, GameMenuActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                onWebSocketClose(1, "Exiting Clan page", true);
                startActivity(intent);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Sends the user input to the clan chat
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                webSocketManager.sendMessage(chatEntry.getText().toString());
            }
        });
        statsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String url = "http://coms-309-041.class.las.iastate.edu:8080/users/" + clanList[spinner_position];

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            /**
                             * Sets the instance variable of the playerObject on a valid Volley response
                             * @param response The JSON object of the player selected
                             */
                            @Override
                            public void onResponse(JSONObject response) {
                                Intent intent = new Intent(ClansActivity.this, StatsActivity.class);
                                intent.putExtra("statsObject", response.toString());
                                intent.putExtra("playerObject", playerObject.toString());
                                intent.putExtra("routeTo", 1);
                                onWebSocketClose(1, "Exiting Clan page", true);
                                startActivity(intent);
                            }
                        },
                        /**
                         * Displays an error that the player could not be updated
                         */
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                errorText.setText("Could not update user's statistical data");
                            }
                        }
                );
                requestQueue.add(jsonObjectRequest);
            }
        });
        friendButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Adds the user selected from the clan member dropdown to the list of the user's friends
             */
            @Override
            public void onClick(View v) {
                String apiUrl = "http://coms-309-041.class.las.iastate.edu:8080/friends/" + username + "/" + clanList[spinner_position];

                // Make a POST request using Volley to add a friend
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST, apiUrl,
                        new Response.Listener<String>() {
                            /**
                             * sends a message when the user is successfully added as a friend
                             * @param response - the string of the user added as a friend.
                             */
                            @Override
                            public void onResponse(String response) {
                                errorText.setText("Friend added");
                            }
                        },
                        new Response.ErrorListener() {
                            /**
                             * sends error message when the friend cant be added.
                             * @param error - text displayed on screen.
                             */
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                errorText.setText("Error Adding Friend");
                                error.printStackTrace();
                            }
                        }
                );

                // Add the request to the RequestQueue
                requestQueue.add(stringRequest);

            }
        });
    }
    /**
     * using the three way handshake a connection is made with a web socket
     * @param handshakedata Information about the server handshake.
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        Log.d("WebSocket", "Connected");
    }
    /**
     * message being sent back from web socket confirming its connected.
     * @param message The received WebSocket message.
     */
    @Override
    public void onWebSocketMessage(String message) {
        // Received a message from WebSocket server
        Log.d("WebSocket", "Received message: " + message);
        runOnUiThread(() -> {
            txChat.append(message + "\n");
            scrollView.fullScroll(View.FOCUS_DOWN);
        });
    }
    /**
     * closes the websocket connection to the app.
     *
     * @param code   The status code indicating the reason for closure.
     * @param reason A human-readable explanation for the closure.
     * @param remote Indicates whether the closure was initiated by the remote endpoint.
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        // WebSocket connection closed
        Log.d("WebSocket", "Closed");
    }

    /**
     * displays error message if the web socket connection fails.
     * @param ex The exception that describes the error.
     */
    @Override
    public void onWebSocketError(Exception ex) {

    }
    /**
     * used to disconnect the web socket when the app is closed completely.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disconnect WebSocket when activity is destroyed
        webSocketManager.disconnectWebSocket();
    }

    public void getOrg(String username){
        String url = "http://coms-309-041.class.las.iastate.edu:8080/userorg/";
        url += username;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    /**
                     * On a successful GET request, the organization text display is updated to show the user's organization
                     * @param response The String of the user's organization from the GET request
                     */
                    @Override
                    public void onResponse(String response) {
                        getClanList(response);
                        startSockets(response);
                        ((TextView)findViewById(R.id.clansTitle)).setText("Clan: " + response);
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * On an unsuccessful GET request, the error is displayed to the text box
                     * @param error The Volley error that caused an unsuccessful GET request
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors


                    }
                }
        );

        // Add the request to the provided Volley queue
        requestQueue.add(stringRequest);
    }
    private void getClanList(String clanName){
        String url = "http://coms-309-041.class.las.iastate.edu:8080/organizations/" + clanName + "/users";

        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    /**
                     * On a successful GET request, the clans username list is set in the instance variable
                     * @param response The json array of strings of the users in the clan
                     */
                    @Override
                    public void onResponse(JSONArray response) {
                        clanList = new String[response.length()];
                        for(int i = 0; i < response.length(); i++){
                            try {
                                JSONObject object =  new JSONObject(response.getString(i));
                                clanList[i] =object.getString("playerUsername");
                            } catch (JSONException e) {
                                errorText.setText("Could not load all clan members");
                            }
                        }
                        adapterSetup(clanList);
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * On an unsuccessful GET request, the error is displayed to the text box
                     * @param error The Volley error that caused an unsuccessful GET request
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        errorText.setText("Could not load all clan members");
                    }
                }
        );

        requestQueue.add(arrayRequest);
    }
    private void startSockets(String clanName){
        // Initialize WebSocketManager
        webSocketManager = WebSocketManager.getInstance();
        // Set WebSocketListener
        webSocketManager.setWebSocketListener(this);
        String serverUrl = "ws://coms-309-041.class.las.iastate.edu:8080/clan/chat/" + clanName + '/' + username;
        webSocketManager.connectWebSocket(serverUrl);
    }
    private void adapterSetup(String[] clanList){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(ClansActivity.this, android.R.layout.simple_spinner_item, clanList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
