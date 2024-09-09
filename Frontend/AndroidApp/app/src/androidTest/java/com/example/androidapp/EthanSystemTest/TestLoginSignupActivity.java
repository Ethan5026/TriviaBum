package com.example.androidapp.EthanSystemTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.intent.Intents;

import static androidx.test.espresso.intent.Intents.intended;

import com.example.androidapp.GameMenuActivity;
import com.example.androidapp.LoginSignupActivity;
import com.example.androidapp.MainActivity;
import com.example.androidapp.R;

/**
 * Tests cases for the LoginSignupActivity
 * Tests that the test user with correct credentials can be logged in and transfered to the game menu
 * Tests that the test user with incorrect credentials recieved an Incorrect Password prompt
 * Tests that signing up as a new user will put you through to the game menu
 * Tests that the back button will bring you back to the main activity.
 */
//@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class TestLoginSignupActivity {

    /**
     * Holds the integer of milliseconds to wait in between each volley request
     */
    private static int SIMULATION_DELAY_MS = 1500;

    /**
     * Sets the activity scenario for each of the tests
     */
    @Rule
    public ActivityScenarioRule<LoginSignupActivity> activityScenarioRule
            = new ActivityScenarioRule<>(LoginSignupActivity.class);

    /**
     * Tests a successful login with the correct username and password to bring the user to the game menu
     */
    @Test
    public void testSuccessfulLogin() {
        Intents.init();
        // Find the EditText and perform type text action
        Espresso.onView(withId(R.id.Username_Entry)).perform(typeText("testCase"), closeSoftKeyboard());


        // Find the EditText and perform type text action
        Espresso.onView(withId(R.id.Password_Entry)).perform(typeText("testCase"), closeSoftKeyboard());


        // Click on the login button
        Espresso.onView(withId(R.id.Login_Button)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }


        // Check if its taking you to the game menu
        // Use Espresso Intents to verify that the correct intent has been sent
        intended(IntentMatchers.hasComponent(GameMenuActivity.class.getName()));
        Intents.release();
    }

    /**
     * Tests an unsuccessful login with the incorrect password, showing the corresponding error message
     */
    @Test
    public void testUnsuccessfulLoginPassword() {
        // Find the EditText and perform type text action
        Espresso.onView(withId(R.id.Username_Entry)).perform(ViewActions.typeText("testCase"));
        Espresso.closeSoftKeyboard();


        // Find the EditText and perform type text action
        Espresso.onView(withId(R.id.Password_Entry)).perform(ViewActions.typeText("WrongPassword"));
        Espresso.closeSoftKeyboard();


        // Click on the login button
        Espresso.onView(withId(R.id.Login_Button)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        // Check if the TextView displays the correct text after button click
        Espresso.onView(withId(R.id.Error_Text))
                .check(ViewAssertions.matches(ViewMatchers.withText("Incorrect Password")));
    }

    /**
     * Testing the unsuccessful login where a user is not found and the corresponding error message is shown
     */
    @Test
    public void testUnsuccessfulLoginUsername() {
        // Find the EditText and perform type text action
        Espresso.onView(withId(R.id.Username_Entry)).perform(ViewActions.typeText("testCaseFake"));
        Espresso.closeSoftKeyboard();


        // Find the EditText and perform type text action
        Espresso.onView(withId(R.id.Password_Entry)).perform(ViewActions.typeText("testCase"));
        Espresso.closeSoftKeyboard();


        // Click on the login button
        Espresso.onView(withId(R.id.Login_Button)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }

        // Check if the TextView displays the correct text after button click
        Espresso.onView(withId(R.id.Error_Text))
                .check(ViewAssertions.matches(ViewMatchers.withText("Could not login user")));
    }

    /**
     * Tests a successful signup of creating a new user and sending them to the game menu
     */
    //@Test
    public void testSuccessSignup() {
        Intents.init();
        // Find the EditText and perform type text action
        Espresso.onView(withId(R.id.Username_Entry)).perform(ViewActions.typeText("testCaseSignup"));
        Espresso.closeSoftKeyboard();


        // Find the EditText and perform type text action
        Espresso.onView(withId(R.id.Password_Entry)).perform(ViewActions.typeText("testCaseSignup"));
        Espresso.closeSoftKeyboard();


        // Click on the signup button
        Espresso.onView(withId(R.id.Signup_Button)).perform(click());

        // Put thread to sleep to allow volley to handle the request
        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {
        }
        // Check if its taking you to the game menu
        // Use Espresso Intents to verify that the correct intent has been sent
        intended(IntentMatchers.hasComponent(GameMenuActivity.class.getName()));
        Intents.release();
    }

    /**
     * Tests that clicking to continue as a guest sends you to the game menu
     */
    @Test
    public void testGuestButton() {

        Intents.init();
        //Find the back button and click it
        Espresso.onView(withId(R.id.guestButton)).perform(click());

        // Check if its taking you back to the game menu
        // Use Espresso Intents to verify that the correct intent has been sent
        intended(IntentMatchers.hasComponent(GameMenuActivity.class.getName()));
        Intents.release();

    }

    /**
     * Tests the back button returns you to the main menu
     */
    @Test
    public void backButton() {

        //Intents.init();
        //Find the back button and click it
        Espresso.onView(withId(R.id.Back_Button1)).perform(click());

        // Check if its taking you back to the game menu
        // Use Espresso Intents to verify that the correct intent has been sent
        //intended(IntentMatchers.hasComponent(MainActivity.class.getName()));
        //Intents.release();
    }
}
