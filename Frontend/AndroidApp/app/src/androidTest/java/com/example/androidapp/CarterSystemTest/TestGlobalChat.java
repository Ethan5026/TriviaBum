package com.example.androidapp.CarterSystemTest;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.androidapp.GlobalChatActivity;
import com.example.androidapp.R;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;

public class TestGlobalChat {
    /**
     * Holds the integer of milliseconds to wait in between each volley request
     */
    private static int SIMULATION_DELAY_MS = 2000;

    @Rule
    public IntentsTestRule<GlobalChatActivity> intentsTestRule = new IntentsTestRule<>(GlobalChatActivity.class, true, false);

    public void launchWithFakePlayer() throws Exception {
        JSONObject fakePlayer = new JSONObject("{\n" +
                "    \"powerup\": {\n" +
                "        \"timeFreezeCount\": 0,\n" +
                "        \"doublePointsCount\": 0,\n" +
                "        \"trollCount\": 0\n" +
                "    }," +
                "    \"playerId\": 1,\n" +
                "    \"playerUsername\": \"testCase\",\n" +
                "    \"playerHashedPassword\": \"eb97d409396a3e5392936dad92b909da6f08d8c121a45e1f088fe9768b0c0339\",\n" +
                "    \"totalQuestions\": 0,\n" +
                "    \"totalCorrect\": 0,\n" +
                "    \"currentStreak\": 0,\n" +
                "    \"longestStreak\": 0,\n" +
                "    \"correctRatio\": 0.0,\n" +
                "    \"friendlist\": \"testCaseFriend\",\n" +
                "    \"admin\": false,\n" +
                "    \"questionMaster\": false\n" +
                "}");

        Intent intent = new Intent();
        intent.putExtra("isGuest", false);
        intent.putExtra("playerObject", fakePlayer.toString());

        intentsTestRule.launchActivity(intent);
    }

    public JSONObject useFakePlayer() throws JSONException {
        JSONObject fakePlayer = new JSONObject();
        fakePlayer.put("playerUsername", "testUser");

        return fakePlayer;
    }

    @Test
    public void testWebSocketConnection() throws Exception {
        launchWithFakePlayer();

        ServerHandshake handshake = null;

        Espresso.onView(ViewMatchers.withId(R.id.bt1)).perform(click());

        String serverUrl = "ws://coms-309-041.class.las.iastate.edu:8080/chat/" + useFakePlayer().toString();

        intentsTestRule.getActivity().webSocketManager.connectWebSocket(serverUrl);

        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        intentsTestRule.getActivity().onWebSocketMessage("Connected");

    }

    @Test
    public void testWebSocketDisconnect() throws Exception {
        launchWithFakePlayer();

        Espresso.onView(withId(R.id.bt3)).perform(click());
        Espresso.onView(withId(R.id.scrollView));
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }
    }

    public String message = "Hello";

    @Test
    public void testMessageSend() throws Exception {
        launchWithFakePlayer();
        Espresso.onView(withId(R.id.et2)).perform(ViewActions.typeText(message), closeSoftKeyboard());
        Espresso.onView(withId(R.id.bt2)).perform(click());

        intentsTestRule.getActivity().onWebSocketMessage(message);
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }
    }
}
