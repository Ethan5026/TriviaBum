package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class implements, controls, and binds, xml objects on the Help Chat screen
 * that helps connect admin and player users over a websocket through a help queue priority.
 */
public class HelpChatActivity extends AppCompatActivity implements WebSocketListener {
    /**
     * The user's object passed into the screen that holds their information
     */
    private JSONObject playerObject;
    /**
     * The Volley Request Queue to perform the CRUDL operations
     */
    private static RequestQueue requestQueue;
    /**
     * The text box for the text to be displayed for websocket connection information
     */
    private static TextView txChat;
    /**
     * The text box for the text to be displayed for websocket data transfer.
     * Has scrolling functionality.
     */
    private static ScrollView scrollView;
    /**
     * The manager the helps connect to a websocket based on a url
     */
    private WebSocketManager webSocketManager;

    /**
     * The username of the user from playerObject
     */
    private String username;
    /**
     * Set to the next user's id in the help queue
     */
    private static String nextUserId;
    /**
     * The text entry box for the user to place a message they want to be
     * sent over the websocket connection.
     */
    private EditText socketMessage;

    /**
     * The creation binding of xml buttons and text boxes to dictate the functionality
     * of the help chat.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpchat);
        uiDesign.applyRandomBackgroundColor(this);


        // Retrieve the JSONObject from the Intent
        Intent intent = getIntent();
        if (intent.hasExtra("playerObject")) {
            String jsonString = intent.getStringExtra("playerObject");

            // Convert the String back to JSONObject
            try {
                assert jsonString != null;
                playerObject = new JSONObject(jsonString);
                username = playerObject.getString("playerUsername");

                // Now you can use the receivedJsonObject as needed

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Button backButton = (Button) findViewById(R.id.backButton);
        Button connectButton = (Button) findViewById(R.id.connectButton);
        Button sendButton = (Button) findViewById(R.id.sendButton);
        txChat = findViewById(R.id.socketText);
        scrollView = findViewById(R.id.scrollView);
        socketMessage = findViewById(R.id.socketMessage);

        // Initialize WebSocketManager
        webSocketManager = WebSocketManager.getInstance();

        // Set WebSocketListener
        webSocketManager.setWebSocketListener(this);
        try {
            if(playerObject.getBoolean("admin")){
                //get the string of the username of next in queue
                connectButton.setText("Connect/Next");
            }
            else{
                connectButton.setText("Connect");
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Sends the user back to the GameMenu page
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpChatActivity.this, GameMenuActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                webSocketManager.disconnectWebSocket();
                startActivity(intent);
            }
        });
        connectButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Attempts to connect the user to the websocket ws://{serverURL}/help/{username}
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                // Connect to WebSocket server
                String serverUrl = null;
                try {
                    int userId= playerObject.getInt("playerId");
                    if(playerObject.getBoolean("admin")){
                        //get the string of the username of next in queue
                        serverUrl = "ws://coms-309-041.class.las.iastate.edu:8080/help/admin/" + userId;
                    }
                    else {
                        serverUrl = "ws://coms-309-041.class.las.iastate.edu:8080/help/queue/" + username;
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                webSocketManager.connectWebSocket(serverUrl);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Attempts to send a message over the websocket connection
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                // Send message
                String message = socketMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    webSocketManager.sendMessage(message);
                    socketMessage.getText().clear();
                }
                runOnUiThread(() -> {
                    txChat.append("[Me] " + message + "\n");
                    scrollView.fullScroll(View.FOCUS_DOWN);
                });
            }
        });

        requestQueue = Volley.newRequestQueue(this);

    }

    /**
     * Logs that the websocket connection is initiated
     * @param handshakedata the data from the TLS handshake performed to intiate a websocket connection
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        // WebSocket connection opened
        Log.d("WebSocket", "Connected");
    }

    /**
     * Writes the incoming websocket message to the scrolling text box
     * @param message The string message coming from the websocket connection
     */
    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            txChat.append(message + "\n");
            scrollView.fullScroll(View.FOCUS_DOWN);
        });
    }

    /**
     * The action performed when a websocket is closed
     * @param code The code for why the websocket closed
     * @param reason The string for why the websocket closed
     * @param remote The boolean value dictating if it was a remote connection
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {

    }

    /**
     * The operation performed when an error from the websocket connection occurs
     * @param ex The exception that the error comes from
     */

    @Override
    public void onWebSocketError(Exception ex) {

    }

    /**
     * The disconnection protocol for closing a websocket.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disconnect WebSocket when activity is destroyed
        webSocketManager.disconnectWebSocket();
    }

    /**
     * Function performing the GET operation to get the string of the username with the user's
     * username. Used to connect admin to the user's websocket in the top of the queue.
     */
    private static void getQueue(){
        String url = "http://coms-309-041.class.las.iastate.edu:8080/helpqueue/";

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,

                new Response.Listener<String>() {
                    /**
                     * On a successful GET request, the next username is set as an instance variable
                     * @param response The Volley response of the String of the next username in the queue
                     */
                    @Override
                    public void onResponse(String response) {
                        nextUserId = response;
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * On an unsuccessful GET request, the error is added to the scrolling text box
                     * @param error The Volley error causing the request to fail
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        txChat.append(error.toString() + "\n");
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                }
        );

        // Add the request to the provided Volley queue
        requestQueue.add(stringRequest);
    }
}
