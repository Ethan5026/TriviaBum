package com.example.androidapp.EthanSystemTest;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.intent.Intents.intended;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import android.content.Intent;

import com.example.androidapp.AdminActivity;
import com.example.androidapp.GameMenuActivity;
import com.example.androidapp.R;

/**
 * Test file containing test cases for the AdminActivity, which include
 * Tests the user creation functionality
 * Tests the user deletion functionality
 * Tests the user update to update its username, password, admin, and question creator privileges
 * Tests the back button returns to the game menu

 */
//@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class TestAdminActivityUsers {

    /**
     * Holds the integer of milliseconds to wait in between each volley request
     */
    private static int SIMULATION_DELAY_MS = 2500;

    /**
     * Sets up scenario for testing
     */
    @Rule
    public ActivityScenarioRule<AdminActivity> activityScenarioRule
            = new ActivityScenarioRule<>(intentStarter());

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
        intent.putExtra("loadUsers", true);
        return intent;
    }

    /**
     * Tests the creation of a new user by an admin and checking for a successful response
     */
    //@Test
    public void testUserCreation(){
        // Perform actions to trigger the post method
        onView(withId(R.id.usernameTextEntry)).perform(typeText("TestUser"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.passwordTextEntry)).perform(typeText("TestUser"));
        Espresso.closeSoftKeyboard();


        onView(withId(R.id.newUserButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        //test response
        // Check if the TextView displays the correct clan name set by default
        onView(withId(R.id.responseText))
                .check(ViewAssertions.matches(ViewMatchers.withText("Success: New user added")));

    }

    /**
     * Deletes the user with the lowest ID, disabled by default to avoid deleting users
     */
    //@Test
    public void testUserDeletion(){
        onView(withId(R.id.configurations)).perform(click());
        onData(Matchers.allOf(is(instanceOf(String.class)))).atPosition(0).perform(click());


        onView(withId(R.id.deleteUserButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        //test response
        // Check if the TextView displays the correct clan name set by default
        onView(withId(R.id.responseText))
                .check(ViewAssertions.matches(ViewMatchers.withText("Successfully deleted user")));
    }

    /**
     * Tests that the user with the lowest ID (first selected in the spinner) can have their username successfully updated by an admin
     */
    @Test
    public void testUserUpdateUsername(){
        onView(withId(R.id.configurations)).perform(click());
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }
        onData(Matchers.allOf(is(instanceOf(String.class)))).atPosition(0).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }
        // Perform actions to trigger the change
        onView(withId(R.id.usernameChangeTextEntry)).perform(ViewActions.clearText());
        onView(withId(R.id.usernameChangeTextEntry)).perform(typeText("testCase2"));

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.usernameChangeButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        //test response
        // Check if the TextView displays the correct clan name set by default
        onView(withId(R.id.responseText))
                .check(ViewAssertions.matches(ViewMatchers.withText("User Successfully edited")));

        // Perform actions to trigger the change
        onView(withId(R.id.usernameChangeTextEntry)).perform(ViewActions.clearText());
        onView(withId(R.id.usernameChangeTextEntry)).perform(typeText("testCase"));

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.usernameChangeButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        //test response
        // Check if the TextView displays the correct clan name set by default
        onView(withId(R.id.responseText))
                .check(ViewAssertions.matches(ViewMatchers.withText("User Successfully edited")));
    }


    /**
     * Tests that the user with the lowest ID (first selected in the spinner) can have their password successfully updated by an admin
     */
    @Test
    public void testUserUpdatePassword(){
        onView(withId(R.id.configurations)).perform(click());
        onData(Matchers.allOf(is(instanceOf(String.class)))).atPosition(0).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }
        // Perform actions to trigger the change
        onView(withId(R.id.passwordChangeTextEntry)).perform(ViewActions.clearText());
        onView(withId(R.id.passwordChangeTextEntry)).perform(typeText("testCase2"));

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.passwordChangeButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        //test response
        // Check if the TextView displays the correct clan name set by default
        onView(withId(R.id.responseText))
                .check(ViewAssertions.matches(ViewMatchers.withText("User Successfully edited")));

        // Perform actions to trigger the change
        onView(withId(R.id.passwordChangeTextEntry)).perform(ViewActions.clearText());
        onView(withId(R.id.passwordChangeTextEntry)).perform(typeText("testCase"));

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.passwordChangeButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        //test response
        // Check if the TextView displays the correct clan name set by default
        onView(withId(R.id.responseText))
                .check(ViewAssertions.matches(ViewMatchers.withText("User Successfully edited")));
    }

    /**
     * Tests that the user with the lowest ID (first selected in the spinner) can have their privileges successfully updated by an admin
     */
    @Test
    public void testUserUpdatePrivileges(){
        onView(withId(R.id.configurations)).perform(click());
        onData(Matchers.allOf(is(instanceOf(String.class)))).atPosition(0).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }
        // Perform actions to trigger the change
        onView(withId(R.id.changeIsAdminCheckbox)).perform(click());
        onView(withId(R.id.changeIsQuestionMasterCheckbox)).perform(click());


        Espresso.closeSoftKeyboard();
        onView(withId(R.id.updatePriviledgesButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        //test response
        // Check if the TextView displays the correct clan name set by default
        onView(withId(R.id.responseText))
                .check(ViewAssertions.matches(ViewMatchers.withText("User Successfully edited")));

        // Perform actions to trigger the change
        onView(withId(R.id.changeIsAdminCheckbox)).perform(click());
        onView(withId(R.id.changeIsQuestionMasterCheckbox)).perform(click());


        Espresso.closeSoftKeyboard();
        onView(withId(R.id.updatePriviledgesButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        //test response
        // Check if the TextView displays the correct clan name set by default
        onView(withId(R.id.responseText))
                .check(ViewAssertions.matches(ViewMatchers.withText("User Successfully edited")));
    }


    /**
     * Tests that an admin can change a user's clan and checks for a successful response
     */
    @Test
    public void testUserClanChange(){
        onView(withId(R.id.configurations)).perform(click());
        onData(Matchers.allOf(is(instanceOf(String.class)))).atPosition(0).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }
        // Perform actions to trigger the change
        onView(withId(R.id.orgChangeTextEntry)).perform(ViewActions.clearText());
        onView(withId(R.id.orgChangeTextEntry)).perform(typeText("2001"));

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.orgChangeButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        //test response
        // Check if the TextView displays the correct clan name set by default
        onView(withId(R.id.responseText))
                .check(ViewAssertions.matches(ViewMatchers.withText("Successfully edited user's organization")));

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.orgLeaveButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

    }

    /**
     * Test function that an admin can make a user leave their clan and checks for a successful response
     */
    @Test
    public void testUserClanLeave(){
        onView(withId(R.id.configurations)).perform(click());
        onData(Matchers.allOf(is(instanceOf(String.class)))).atPosition(0).perform(click());
        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }
        // Perform actions to trigger the change
        onView(withId(R.id.orgChangeTextEntry)).perform(ViewActions.clearText());
        onView(withId(R.id.orgChangeTextEntry)).perform(typeText("2001"));

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.orgChangeButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.orgLeaveButton)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }
        // Check if the TextView displays the correct clan name set by default
        onView(withId(R.id.responseText))
                .check(ViewAssertions.matches(ViewMatchers.withText("Successfully edited user's organization")));
    }

    /**
     * Checks that an admin user can navigate through the clans and users admin settings within the AdminActivity
     */
    @Test
    public void testUserClanNavigation(){
        Intents.init();
        //Find the clans button and click it
        onView(withId(R.id.orgsButton)).perform(click());

        // Check if its refreshing the admin activity
        // Use Espresso Intents to verify that the correct intent has been sent
        intended(IntentMatchers.hasComponent(AdminActivity.class.getName()));
        Intents.release();

        //Check if a clan specific element is shown
        onView(withId(R.id.clanTextEntry)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    }

    /**
     * Tests the navigational component of the back button returning the user to the game menu
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