package com.example.androidapp.CarterSystemTest;

import android.content.Intent;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.intent.Intents;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;

import com.example.androidapp.CreditsActivity;
import com.example.androidapp.LoginSignupActivity;
import com.example.androidapp.MainActivity;
import com.example.androidapp.R;

@RunWith(AndroidJUnit4.class)
public class TestMain {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void testNavigateToLoginSignupActivity() {
        // Click the "Play" button
        onView(ViewMatchers.withId(R.id.play_Button)).perform(click());

        // Check if the intent to start the LoginSignupActivity was sent
        intended(hasComponent(LoginSignupActivity.class.getName()));
    }

    @Test
    public void testNavigateToCreditsActivity() {
        // Click the "Credits" button
        onView(withId(R.id.credits_Button)).perform(click());

        // Check if the intent to start the CreditsActivity was sent
        //intended(hasComponent(CreditsActivity.class.getName()));
    }
}
