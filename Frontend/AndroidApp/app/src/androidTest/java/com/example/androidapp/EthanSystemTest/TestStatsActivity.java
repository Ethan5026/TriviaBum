package com.example.androidapp.EthanSystemTest;
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
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.Intents.intended;
import android.content.Intent;

import com.example.androidapp.GameMenuActivity;
import com.example.androidapp.R;
import com.example.androidapp.StatsActivity;


/**
 * Tests cases for the StatsActivity
 * Tests that the user's stats are displayed and they can view the stats of their friends
 * Tests that the user is routed back to the game menu activity
 */
//@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class TestStatsActivity {

    /**
     * Holds the integer of milliseconds to wait in between each volley request
     */
    private static int SIMULATION_DELAY_MS = 1500;

    /**
     * Sets up scenario for testing
     */
    @Rule
    public ActivityScenarioRule<StatsActivity> activityScenarioRule
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
        intent = new Intent(ApplicationProvider.getApplicationContext(), StatsActivity.class);
        intent.putExtra("playerObject", playerObject.toString());
        intent.putExtra("statsObject", playerObject.toString());
        intent.putExtra("routeTo", 0);
        return intent;
    }

    /**
     * Test case that a user can view their stats by clicking My Stats
     */
    @Test
    public void testStatsDisplay() {

        //Test All Players
        Espresso.onView(ViewMatchers.withId(R.id.myStats)).perform(ViewActions.click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        // Check if the TextView displays that the leaderboard was correctly updated
        Espresso.onView(withId(R.id.errorBox))
                .check(ViewAssertions.matches(ViewMatchers.withText("Retrieved Stats Successfully")));
    }

    /**
     * Test case that a user can view their friends stats by clicking load friend's stats
     */
    @Test
    public void testFriendsStatsDisplay() {

        //Test All Players
        Espresso.onView(withId(R.id.friendButton)).perform(ViewActions.click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        // Check if the TextView displays that the leaderboard was correctly updated
        Espresso.onView(withId(R.id.errorBox))
                .check(ViewAssertions.matches(ViewMatchers.withText("Retrieved Stats Successfully")));
    }

    /**
     * Tests that the back button will return you to the game menu
     */
    @Test
    public void testBackButton(){

        Intents.init();
        //Find the back button and click it
        Espresso.onView(withId(R.id.backButton)).perform(ViewActions.click());

        // Check if its taking you back to the game menu
        // Use Espresso Intents to verify that the correct intent has been sent
        intended(IntentMatchers.hasComponent(GameMenuActivity.class.getName()));
        Intents.release();

    }


}
