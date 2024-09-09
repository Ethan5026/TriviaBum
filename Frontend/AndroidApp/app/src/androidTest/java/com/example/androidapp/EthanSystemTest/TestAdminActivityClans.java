package com.example.androidapp.EthanSystemTest;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.Intents.intended;
import static org.hamcrest.Matchers.allOf;
import android.content.Intent;
import android.view.View;

import com.example.androidapp.AdminActivity;
import com.example.androidapp.GameMenuActivity;
import com.example.androidapp.R;


/**
 * Test file containing test cases for the AdminActivity, which include
 * Tests changing the clan of the user
 * Tests a user leaving it's clan
 * Tests navigation between the users and clans pages
 * Tests creating a new clan
 * Tests editing a clan's name
 * Tests deleting a clan
 */
//@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class TestAdminActivityClans {

    /**
     * Holds the integer of milliseconds to wait in between each volley request
     */
    private static int SIMULATION_DELAY_MS = 1500;

    /**
     * Sets up scenario for testing
     */
    @Rule
    public ActivityScenarioRule<AdminActivity> activityScenarioRule
            = new ActivityScenarioRule<>(intentStarter());

    private View decorView;

    /**
     * Sets up the intent with the player object inputted as an extra
     *
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
        intent = new Intent(ApplicationProvider.getApplicationContext(), AdminActivity.class);
        intent.putExtra("playerObject", playerObject.toString());
        intent.putExtra("loadUsers", false);
        return intent;
    }

    /**
     * Test case for an admin to navigate between clan and user settings on the AdminActivity
     */
    @Test
    public void testUserClanNavigation(){
        Intents.init();
        //Find the clans button and click it
        onView(withId(R.id.usersButton)).perform(click());

        // Check if its refreshing the admin activity
        // Use Espresso Intents to verify that the correct intent has been sent
        intended(IntentMatchers.hasComponent(AdminActivity.class.getName()));
        Intents.release();

        //Check if a clan specific element is shown
        onView(withId(R.id.usernameTextEntry)).check(matches(isDisplayed()));

    }

    /**
     * Tests the creation of a new clan by the admin and checks for a successful response
     */
    //@Test
    public void testClanCreation(){

        // Perform actions to trigger the post method
        onView(withId(R.id.clanTextEntry)).perform(typeText("TestClan"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.newClanButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        //test response
        // Check if the TextView displays the correct clan name set by default
        onView(withId(R.id.responseText))
                .check(matches(ViewMatchers.withText("Success: Clan added")));
    }

    /**
     * Tests the updating of a clan's name by an admin and checking for a correct response
     */
    @Test
    public void testClanUpdate(){
        onView(withId(R.id.configurations)).perform(click());
        onData(Matchers.allOf(is(instanceOf(String.class)))).atPosition(0).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }
        // Perform actions to trigger the change
        onView(withId(R.id.clanChangeTextEntry)).perform(ViewActions.clearText());
        onView(withId(R.id.clanChangeTextEntry)).perform(typeText("TestClanUpdate2"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.clanChangeButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        //test response
        // Check if the TextView displays the correct clan name set by default
        onView(withId(R.id.responseText))
                .check(matches(ViewMatchers.withText("Success: Clan name changed")));

        // Perform actions to trigger the change
        onView(withId(R.id.clanChangeTextEntry)).perform(ViewActions.clearText());
        onView(withId(R.id.clanChangeTextEntry)).perform(typeText("TestClanUpdate"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.clanChangeButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        //test response
        // Check if the TextView displays the correct clan name set by default
        onView(withId(R.id.responseText))
                .check(matches(ViewMatchers.withText("Success: Clan name changed")));
    }

    /**
     * Deletes the clan with the lowest ID, disabled by defualt to avoid deleting a wanted clan
     */
    //@Test
    public void testClanDeletion(){
        onView(withId(R.id.configurations)).perform(click());
        onData(Matchers.allOf(is(instanceOf(String.class)))).atPosition(0).perform(click());


        onView(withId(R.id.clanDeleteButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        //test response
        // Check if the TextView displays the correct clan name set by default
        onView(withId(R.id.responseText))
                .check(matches(ViewMatchers.withText("Success: Clan deleted")));
    }

    /**
     * Tests the navigational back button to return the user to the game menu
     */
    @Test
    public void testBackButton(){
        Intents.init();
        //Find the back button and click it
        onView(withId(R.id.backButton)).perform(click());

        // Check if its taking you back to the game menu
        // Use Espresso Intents to verify that the correct intent has been sent
        intended(IntentMatchers.hasComponent(GameMenuActivity.class.getName()));
        Intents.release();
    }

}