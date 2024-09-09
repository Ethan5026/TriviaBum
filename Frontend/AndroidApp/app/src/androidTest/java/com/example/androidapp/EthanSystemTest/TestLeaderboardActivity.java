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
import com.example.androidapp.LeaderboardActivity;
import com.example.androidapp.R;


/**
 * Tests cases for the LeaderboardActivity
 * Tests that leaderboard values are shown when a user clicks all options to view each category of leaderboards
 */
//@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class TestLeaderboardActivity {

    /**
     * Holds the integer of milliseconds to wait in between each volley request
     */
    private static int SIMULATION_DELAY_MS = 1500;

    /**
     * Sets up scenario for testing
     */
    @Rule
    public ActivityScenarioRule<LeaderboardActivity> activityScenarioRule
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
        intent = new Intent(ApplicationProvider.getApplicationContext(), LeaderboardActivity.class);
        intent.putExtra("playerObject", playerObject.toString());
        return intent;
    }

    /**
     * Test case that a user can click on each leaderboard search setting and values are displayed
     */
    @Test
    public void testLeaderboardDisplay(){

        //Test All Players
        Espresso.onView(ViewMatchers.withId(R.id.allPlayersButton)).perform(ViewActions.click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}

        // Check if the TextView displays that the leaderboard was correctly updated
        Espresso.onView(withId(R.id.errorBox))
                .check(ViewAssertions.matches(ViewMatchers.withText("Leaderboard Updated: All Players")));

        //Test Top Organizations
        Espresso.onView(withId(R.id.orgsButton)).perform(ViewActions.click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}

        // Check if the TextView displays that the leaderboard was correctly updated
        Espresso.onView(withId(R.id.errorBox))
                .check(ViewAssertions.matches(ViewMatchers.withText("Leaderboard Updated: Top Clans")));

        //Test Top Players in clans
        Espresso.onView(withId(R.id.myOrgButton)).perform(ViewActions.click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}

        // Check if the TextView displays that the leaderboard was correctly updated
        Espresso.onView(withId(R.id.errorBox))
                .check(ViewAssertions.matches(ViewMatchers.withText("Leaderboard Updated: My Clan")));

        //Test Top in Friends list
        Espresso.onView(withId(R.id.myFriendsButton)).perform(ViewActions.click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}

        // Check if the TextView displays that the leaderboard was correctly updated
        Espresso.onView(withId(R.id.errorBox))
                .check(ViewAssertions.matches(ViewMatchers.withText("Leaderboard Updated: My Friends")));

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
