package com.example.androidapp;

import org.java_websocket.handshake.ServerHandshake;

public interface WebSocketScoreListener {

    void onWebSocketScoreOpen(ServerHandshake handshakedata);

    void onWebSocketScoreClose(int code, String reason, boolean remote);

    void onWebSocketScoreError(Exception ex);

    void onWebSocketScore(int score);
}