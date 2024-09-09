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
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

//import kotlinx.coroutines.scheduling.GlobalQueue;

/**
 * Class that implements a global chat room for Users through the game menu.
 */
public class GlobalChatActivity extends AppCompatActivity implements WebSocketListener {

    /**
     * message text being typed to send
     */
    private EditText etMessage;

    /**
     * button to connect the user to the chat page.
     */
    private Button btnConnect;

    /**
     * button to send the message.
     */
    private Button btnSend;

    /**
     * message being displayed
     */
    private TextView txChat;

    /**
     * shows list of messages
     */
    private ScrollView scrollView;

    /**
     * object that stores player data.
     */
    private JSONObject playerObject;

    /**
     * username string from playerObject.
     */
    private String username;

    /**
     * loads the websocket manager.
     */
    public WebSocketManager webSocketManager;

    /**
     * loads the activity and view from the xml file and manifest files.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_globalchat);

        uiDesign.applyRandomBackgroundColor(this);


        Intent intent = getIntent();
        if (intent.hasExtra("playerObject")) {
            String jsonString = intent.getStringExtra("playerObject");

            try {
                assert jsonString != null;
                playerObject = new JSONObject(jsonString);
                username = playerObject.getString("playerUsername");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        etMessage = findViewById(R.id.et2);
        btnConnect = findViewById(R.id.bt1);
        btnSend = findViewById(R.id.bt2);
        txChat = findViewById(R.id.tx1);
        scrollView = findViewById(R.id.scrollView);
        Button backButton = findViewById(R.id.bt3);


        // Initialize WebSocketManager
        webSocketManager = WebSocketManager.getInstance();

        // Set WebSocketListener
        webSocketManager.setWebSocketListener(this);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            /**
             * connects the user to the chat using their username.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                // Connect to WebSocket server
                String serverUrl = "ws://coms-309-041.class.las.iastate.edu:8080/chat/" + username;
                webSocketManager.connectWebSocket(serverUrl);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * connects the user to the chat using their username.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                // Connect to WebSocket server
                Intent intent = new Intent(GlobalChatActivity.this, GameMenuActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                webSocketManager.disconnectWebSocket();
                startActivity(intent);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            /**
             * sends the message to the chat that the user sends.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                // Send message
                String message = etMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    webSocketManager.sendMessage(message);
                    etMessage.getText().clear();
                }
            }
        });
    }

    /**
     * using the three way handshake a connection is made with a web socket
     * @param handshakedata Information about the server handshake.
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        // WebSocket connection opened
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
        // WebSocket error occurred
        Log.e("WebSocket", "Error: " + ex.getMessage());
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
}
