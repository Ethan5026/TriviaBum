package com.example.androidapp;

import android.util.Log;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;

public class WebSocketScore {

    private static WebSocketScore instance;
    private MyWebSocketScoreClient webSocketScoreClient;
    private WebSocketScoreListener webSocketScoreListener;

    private WebSocketScore() {}

    public static synchronized WebSocketScore getInstance() {
        if (instance == null) {
            instance = new WebSocketScore();
        }
        return instance;
    }

    public void setWebSocketScoreListener(WebSocketScoreListener listener) {
        this.webSocketScoreListener = listener;
    }

    public void connectWebSocket(String serverUrl) {
        URI uri;
        try {
            uri = new URI(serverUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        webSocketScoreClient = new MyWebSocketScoreClient(uri);
        webSocketScoreClient.connect();
    }

    public void disconnectScoreWebSocket() {
        if (webSocketScoreClient != null) {
            webSocketScoreClient.close();
        }
    }

    public void sendScore(String score){
        if (webSocketScoreClient != null && webSocketScoreClient.isOpen()) {
            webSocketScoreClient.send(score);
        }
    }

    private class MyWebSocketScoreClient extends WebSocketClient {

        private MyWebSocketScoreClient(URI serverUri) {
            super(serverUri);
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            Log.d("WebSocket", "Connected");
            if (webSocketScoreListener != null) {
                webSocketScoreListener.onWebSocketScoreOpen(handshakedata);
            }
        }

        @Override
        public void onMessage(String score) {
            Log.d("WebSocket", "Received score: " + score);
            if (webSocketScoreListener != null) {
                try {
                    int scoreValue = Integer.parseInt(score);
                    webSocketScoreListener.onWebSocketScore(scoreValue);
                } catch (NumberFormatException e) {
                    Log.e("WebSocket", "Invalid score format: " + score);
                }
            }
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            Log.d("WebSocket", "Closed");
            if (webSocketScoreListener != null) {
                webSocketScoreListener.onWebSocketScoreClose(code, reason, remote);
            }
        }

        @Override
        public void onError(Exception ex) {
            Log.e("WebSocket", "Error", ex);
            if (webSocketScoreListener != null) {
                webSocketScoreListener.onWebSocketScoreError(ex);
            }
        }
    }
}
