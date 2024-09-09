package com.example.androidapp.EthanSystemTest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import androidx.test.espresso.intent.Intents;
import static androidx.test.espresso.intent.Intents.intended;
import android.content.Intent;

import com.example.androidapp.AccountSettingsActivity;
import com.example.androidapp.GameMenuActivity;
import com.example.androidapp.LoginSignupActivity;
import com.example.androidapp.R;


/**
 * Tests cases for the AccountSettingsActivity
 * Tests that the username is visually updated and message is recieved when attempting to update the username
 * Tests that two identical password entries displays "Changed Password" after the volley attempt
 * Tests that two unidentical password entries displays an error message
 * Tests that the correct clan name is shown at the beginning
 * Tests that the correct clan name is updated after changing it
 * Tests that the default clan name is shown after leaving a clan
 * An error is shown when attempting to add a unknown clan
 * Tests that the back button returns you to the game menu
 */
//@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class TestAccountSettingsActivity {

    /**
     * Holds the integer of milliseconds to wait in between each volley request
     */
    private static int SIMULATION_DELAY_MS = 1500;

    /**
     * Sets up scenario for testing
     */
    @Rule
    public ActivityScenarioRule<AccountSettingsActivity> activityScenarioRule
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
        intent = new Intent(ApplicationProvider.getApplicationContext(), AccountSettingsActivity.class);
        intent.putExtra("playerObject", playerObject.toString());
        return intent;
    }

    /**
     * The test to update a username and check if the success message and username title was updated
     */
    @Test
    public void testUpdateUsername() {
        // Find the EditText and perform type text action
        Espresso.onView(withId(R.id.usernameChangeTextEntry)).perform(typeText("testCase2"));
        Espresso.closeSoftKeyboard();


        // Click on the change button
        Espresso.onView(withId(R.id.usernameChangeButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}

        // Check if the TextView displays the correct clan name set by default
        Espresso.onView(withId(R.id.usernameChangeResponseText))
                .check(ViewAssertions.matches(ViewMatchers.withText("Success! Changed to testCase2")));


        // Check if the TextView displays the correct clan name set by default
        Espresso.onView(withId(R.id.usernameChangeText))
                .check(ViewAssertions.matches(ViewMatchers.withText("Username: testCase2")));



        // Find the EditText and perform type text action
        Espresso.onView(withId(R.id.usernameChangeTextEntry)).perform(ViewActions.clearText());
        Espresso.onView(withId(R.id.usernameChangeTextEntry)).perform(typeText("testCase"));
        Espresso.closeSoftKeyboard();


        // Click on the change button
        Espresso.onView(withId(R.id.usernameChangeButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}

    }

    /**
     * Testing unidentical passwords and attempting to do a password change. Checks for the error message
     */
    @Test
    public void testUnsuccessfulUpdatePassword() {
        // Find the EditText and perform type text action
        Espresso.onView(withId(R.id.passwordChangeTextEntry)).perform(typeText("testCase2"));
        Espresso.closeSoftKeyboard();

        // Find the EditText and perform type text action
        Espresso.onView(withId(R.id.passwordChangeTextEntry2)).perform(typeText("testCase3"));
        Espresso.closeSoftKeyboard();

        // Click on the change button
        Espresso.onView(withId(R.id.passwordChangeButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}

        // Check if the TextView displays the correct clan name set by default
        Espresso.onView(withId(R.id.passwordChangeResponseText))
                .check(ViewAssertions.matches(ViewMatchers.withText("Passwords do not match")));
    }

    /**
     * Tests two identical password inputs and requests a password change, views the correct output message
     */
    @Test
    public void testSuccessfulUpdatePassword(){
        // Find the EditText and perform type text action
        Espresso.onView(withId(R.id.passwordChangeTextEntry)).perform(typeText("testCase2"));
        Espresso.closeSoftKeyboard();

        // Find the EditText and perform type text action
        Espresso.onView(withId(R.id.passwordChangeTextEntry2)).perform(typeText("testCase2"));
        Espresso.closeSoftKeyboard();

        // Click on the change button
        Espresso.onView(withId(R.id.passwordChangeButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}

        // Check if the TextView displays the correct clan name set by default
        Espresso.onView(withId(R.id.passwordChangeResponseText))
                .check(ViewAssertions.matches(ViewMatchers.withText("Success! Password Changed")));

        // Find the EditText and perform type text action
        Espresso.onView(withId(R.id.passwordChangeTextEntry)).perform(ViewActions.clearText());
        Espresso.onView(withId(R.id.passwordChangeTextEntry)).perform(typeText("testCase"));
        Espresso.closeSoftKeyboard();

        // Find the EditText and perform type text action
        Espresso.onView(withId(R.id.passwordChangeTextEntry2)).perform(ViewActions.clearText());

        Espresso.onView(withId(R.id.passwordChangeTextEntry2)).perform(typeText("testCase"));
        Espresso.closeSoftKeyboard();

        // Click on the change button
        Espresso.onView(withId(R.id.passwordChangeButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}

        // Check if the TextView displays the correct clan name set by default
        Espresso.onView(withId(R.id.passwordChangeResponseText))
                .check(ViewAssertions.matches(ViewMatchers.withText("Success! Password Changed")));

    }

    /**
     * Tests that the clan name appears on creation of the page
     */
    @Test
    public void testClanMessage(){
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}

        // Check if the TextView displays the correct clan name set by default
        Espresso.onView(withId(R.id.orgChangeText))
                .check(ViewAssertions.matches(ViewMatchers.withText("Clan: default")));
    }

    /**
     * Tests that an unknown clan ID will display an error message when attempting to change to it
     */
    @Test
    public void testUnsuccessfulUpdateClan(){
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}
        // Enter the ID of a not real test clan and check if its updated the text
        Espresso.onView(withId(R.id.orgChangeTextEntry)).perform(typeText("252231"));
        Espresso.closeSoftKeyboard();

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}
        //Click the change clan button
        Espresso.onView(withId(R.id.orgChangeButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}

        // Check if the TextView displays the correct clan name set by default
        Espresso.onView(withId(R.id.orgChangeResponseText))
                .check(ViewAssertions.matches(ViewMatchers.withText("Could not change clan")));
    }

    /**
     * Tests that when you update your clan, the corresponding clan name is shown
     */
    @Test
    public void testUpdateClan() {
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}
        // Enter the ID of the test clan and check if its updated the text
        Espresso.onView(withId(R.id.orgChangeTextEntry)).perform(typeText("2001"));
        Espresso.closeSoftKeyboard();


        //Click the change clan button
        Espresso.onView(withId(R.id.orgChangeButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        // Check if the TextView displays the correct clan name set by default
        Espresso.onView(withId(R.id.orgChangeText))
                .check(ViewAssertions.matches(ViewMatchers.withText("Clan: TestClanUpdate")));
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }
        //reset to default by leaving the clan
        Espresso.onView(withId(R.id.orgLeaveButton)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}

    }

    /**
     * Tests that when you are in a clan other than default and leave it, it will display you are in the default clan
     */
    @Test
    public void testLeaveClan(){
        // Enter the ID of the test clan and check if its updated the text
        Espresso.onView(withId(R.id.orgChangeTextEntry)).perform(typeText("2001"));
        Espresso.closeSoftKeyboard();


        //Click the change clan button
        Espresso.onView(withId(R.id.orgChangeButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}

        //reset to default by leaving the clan
        Espresso.onView(withId(R.id.orgLeaveButton)).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}

        // Check if the TextView displays the correct clan name set by default
        Espresso.onView(withId(R.id.orgChangeText))
                .check(ViewAssertions.matches(ViewMatchers.withText("Clan: default")));
    }


    /**
     * Tests that the back button will return you to the game menu
     */
    @Test
    public void testBackButton(){

        Intents.init();
        //Find the back button and click it
        Espresso.onView(withId(R.id.backButton)).perform(click());

        // Check if its taking you back to the game menu
        // Use Espresso Intents to verify that the correct intent has been sent
        intended(IntentMatchers.hasComponent(GameMenuActivity.class.getName()));
        Intents.release();

    }


    /**
     * Tests the deletion of a user. Disabled by default to not allow the test user to be deleted
     */
    //@Test
    public void testDeleteUser(){

        Intents.init();
        //Find the back button and click it
        Espresso.onView(withId(R.id.deleteButton)).perform(click());

        // Check if its taking you back to the game menu
        // Use Espresso Intents to verify that the correct intent has been sent
        intended(IntentMatchers.hasComponent(LoginSignupActivity.class.getName()));
        Intents.release();

    }


}
