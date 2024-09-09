package com.example.androidapp.EthanSystemTest;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.Intents.intended;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import android.content.Intent;

import com.example.androidapp.ClansActivity;
import com.example.androidapp.GameMenuActivity;
import com.example.androidapp.R;
import com.example.androidapp.StatsActivity;


/**
 * Tests cases for the Clan Activity
 */
//@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class TestClanActivity {

    /**
     * Holds the integer of milliseconds to wait in between each volley request
     */
    private static int SIMULATION_DELAY_MS = 1500;

    /**
     * Sets up scenario for testing
     */
    @Rule
    public ActivityScenarioRule<ClansActivity> activityScenarioRule
            = new ActivityScenarioRule<>(intentStarter());

    /**
     * Sets up the intent with the player object inputted as an extra
     * @return the intent for the AccountSettingsActivity
     */
    private Intent intentStarter() {

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
                    "    \"totalQuestions\": 0,\n" +
                    "    \"totalCorrect\": 0,\n" +
                    "    \"currentStreak\": 0,\n" +
                    "    \"longestStreak\": 0,\n" +
                    "    \"correctRatio\": 0.0,\n" +
                    "    \"friendlist\": \"testCaseFriend\",\n" +
                    "    \"admin\": false,\n" +
                    "    \"questionMaster\": false\n" +
                    "}");
        } catch (JSONException e) {
            return new Intent();
        }
        // Create an intent with the extra "playerObject"
        Intent intent = null;
        intent = new Intent(ApplicationProvider.getApplicationContext(), ClansActivity.class);
        intent.putExtra("playerObject", playerObject.toString());
        return intent;
    }

    /**
     * Tests that the add friend button gives a successful response
     */
    @Test
    public void testFriendAddButton(){

        onView(ViewMatchers.withId(R.id.usernameSpinner)).perform(click());
        onData(Matchers.allOf(is(instanceOf(String.class)))).atPosition(0).perform(click());

        Espresso.onView(withId(R.id.friendButton)).perform(ViewActions.click());
    }

    /**
     * Tests that the send button produces no errors when sending a message over the websocket
     */
    @Test
    public void testSendButton(){

    }
    /**
     * Tests that the friends stats will send the user to the stats activity
     */
    @Test
    public void testMemberStatsButton(){

        Intents.init();

        onView(withId(R.id.usernameSpinner)).perform(click());
        onData(Matchers.allOf(is(instanceOf(String.class)))).atPosition(0).perform(click());

        Espresso.onView(withId(R.id.friendStatsButton)).perform(ViewActions.click());

        // Check if its taking you back to the game menu
        // Use Espresso Intents to verify that the correct intent has been sent
        intended(hasComponent(StatsActivity.class.getName()));
        Intents.release();

    }

    /**
     * Tests that the send and back button will return you to the game menu
     */
    @Test
    public void testSendBackButton(){
        Intents.init();

        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }
        Espresso.onView(withId(R.id.sendButton)).perform(ViewActions.click());
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }
        //Find the back button and click it
        Espresso.onView(withId(R.id.back_button)).perform(ViewActions.click());
        Intents.release();


    }


}
