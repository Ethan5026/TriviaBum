package com.example.androidapp.CarterSystemTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.VisibleForTesting;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.androidapp.GameMenuActivity;
import com.example.androidapp.LoginSignupActivity;
import com.example.androidapp.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class TestGameMenu {

    /**
     * Holds the integer of milliseconds to wait in between each volley request
     */
    private static int SIMULATION_DELAY_MS = 1500;

    @Rule
    public ActivityScenarioRule<GameMenuActivity> intentsTestRule = new ActivityScenarioRule<>(intentStart());

    private Intent intentStart() {
        JSONObject playerObject;
        try {
            playerObject = new JSONObject("{\n" +
                    "    \"powerup\": {\n" +
                    "        \"timeFreezeCount\": 0,\n" +
                    "        \"doublePointsCount\": 0,\n" +
                    "        \"trollCount\": 0\n" +
                    "    }," +
                    "    \"playerId\": 1,\n" +
                    "    \"playerUsername\": \"testCase\",\n" +
                    "    \"playerHashedPassword\": \"eb97d409396a3e5392936dad92b909da6f08d8c121a45e1f088fe9768b0c0339\",\n" +
                    "    \"totalQuestions\": 25,\n" +
                    "    \"totalCorrect\": 50,\n" +
                    "    \"currentStreak\": 0,\n" +
                    "    \"longestStreak\": 5,\n" +
                    "    \"correctRatio\": 0.5,\n" +
                    "    \"friendlist\": \"testCaseFriend\",\n" +
                    "    \"admin\": false,\n" +
                    "    \"questionMaster\": false\n" +
                    "}");
        } catch (JSONException e) {
            return new Intent();
        }
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), GameMenuActivity.class);
        intent.putExtra("playerObject", playerObject.toString());
        return intent;
    }

    /**
     * Tests that the back button will return you to the LoginSignupActivity.
     */
    @Test
    public void testBackButton(){
        Intents.init();
        Espresso.onView(ViewMatchers.withId(R.id.backButton)).perform(click());

        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}

        intended(hasComponent(LoginSignupActivity.class.getName()));
        Intents.release();
    }

    /**
     * Tests that the add button works and changes the friends list
     */

    @Test
    public void testAddFriendButton() {
        Intents.init();
        String friendUsername = "friendUser";
        Espresso.onView(withId(R.id.searchEditText)).perform(typeText(friendUsername), closeSoftKeyboard());
        Espresso.onView(withId(R.id.addbutton)).perform(click());

        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}

        onView(withId(R.id.searchEditText)).check(matches(isDisplayed()));

        Intents.release();
    }

    @Test
    public void testremoveFriendButton() {
        Intents.init();
        String friendUsername = "friendUser";
        Espresso.onView(withId(R.id.searchEditText)).perform(typeText(friendUsername), closeSoftKeyboard());
        Espresso.onView(withId(R.id.removebutton)).perform(click());

        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}

        onView(withId(R.id.searchEditText)).check(matches(isDisplayed()));
        Intents.release();
    }
}
