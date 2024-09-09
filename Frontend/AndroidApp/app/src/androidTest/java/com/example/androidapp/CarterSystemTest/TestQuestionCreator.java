package com.example.androidapp.CarterSystemTest;

import static android.widget.Toast.makeText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;
import android.view.View;
import android.widget.Toast.*;
import android.widget.Toast;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;

import com.example.androidapp.FillBlankActivity;
import com.example.androidapp.GameMenuActivity;
import com.example.androidapp.MultipleChoiceActivity;
import com.example.androidapp.QuestionCreatorActivity;
import com.example.androidapp.R;
import com.example.androidapp.SelectQuestionsActivity;
import com.example.androidapp.TrueFalseActivity;

import org.hamcrest.Matcher;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;

public class TestQuestionCreator {

    /**
     * Holds the integer of milliseconds to wait in between each volley request
     */
    private static int SIMULATION_DELAY_MS = 1500;

    @Rule
    public IntentsTestRule<QuestionCreatorActivity> intentsTestRule = new IntentsTestRule<>(QuestionCreatorActivity.class, true, false);

    @Rule
    public IntentsTestRule<MultipleChoiceActivity> multipleChoiceIntentsTestRule = new IntentsTestRule<>(MultipleChoiceActivity.class, true, false);

    @Rule
    public IntentsTestRule<SelectQuestionsActivity> selectQuestionsIntentsTestRule = new IntentsTestRule<>(SelectQuestionsActivity.class, true, false);

    @Rule
    public IntentsTestRule<TrueFalseActivity> trueFalseIntentsTestRule = new IntentsTestRule<>(TrueFalseActivity.class, true, false);

    @Rule
    public IntentsTestRule<FillBlankActivity> fillBlankIntentsTestRule = new IntentsTestRule<>(FillBlankActivity.class, true, false);

    private void launchWithFakePlayer(Class activityClass) throws Exception {
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
                "    \"longestStreak\": 5,\n" +
                "    \"correctRatio\": 0.0,\n" +
                "    \"friendlist\": \"testCaseFriend\",\n" +
                "    \"admin\": false,\n" +
                "    \"questionMaster\": false\n" +
                "}");

        Intent intent = new Intent();
        intent.putExtra("isGuest", false);
        intent.putExtra("playerObject", fakePlayer.toString());

        if (activityClass == QuestionCreatorActivity.class) {
            intentsTestRule.launchActivity(intent);
        } else if (activityClass == MultipleChoiceActivity.class) {
            multipleChoiceIntentsTestRule.launchActivity(intent);
        }  else if (activityClass == SelectQuestionsActivity.class) {
            selectQuestionsIntentsTestRule.launchActivity(intent);
        } else if (activityClass == TrueFalseActivity.class) {
            trueFalseIntentsTestRule.launchActivity(intent);
        } else if (activityClass == FillBlankActivity.class) {
            fillBlankIntentsTestRule.launchActivity(intent);
        }
    }

    /**
     * Tests that the back button will return you to the login signup
     */
    @Test
    public void testBackButton() throws Exception {
        launchWithFakePlayer(QuestionCreatorActivity.class);

        Espresso.onView(ViewMatchers.withId(R.id.backButton)).perform(click());

        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}

        intended(hasComponent(GameMenuActivity.class.getName()));
    }

    @Test
    public void testSaveMultiQuestion() throws Exception {
        launchWithFakePlayer(MultipleChoiceActivity.class);

        // Input some data into the EditTexts
        Espresso.onView(withId(R.id.editTextQuestion))
                .perform(ViewActions.typeText("What is the capital of France"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.editTextCorrect))
                .perform(ViewActions.typeText("Paris"), ViewActions.closeSoftKeyboard());

        Espresso.onView(withId(R.id.editButton)).perform(click());

    }

    @Test
    public void testEditMultiQuestion() throws Exception {
        launchWithFakePlayer(MultipleChoiceActivity.class);

        Espresso.onView(withId(R.id.editquestionid))
                .perform(ViewActions.typeText("123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.editTextCorrect))
                .perform(ViewActions.typeText("Sun"), ViewActions.closeSoftKeyboard());

        // Click the edit button
        Espresso.onView(withId(R.id.editButton)).perform(click());
    }

    @Test
    public void testNavigateBackFromMultipleChoice() throws Exception {
        launchWithFakePlayer(MultipleChoiceActivity.class);

        Espresso.onView(withId(R.id.backButton)).perform(click());
        intended(hasComponent(QuestionCreatorActivity.class.getName()));
    }

    @Test
    public void testNavigateBackFromSelect() throws Exception {
        launchWithFakePlayer(SelectQuestionsActivity.class);

        Espresso.onView(withId(R.id.backButton)).perform(click());
        intended(hasComponent(QuestionCreatorActivity.class.getName()));
    }

    @Test
    public void testNavigateBackFromTrueFalse() throws Exception {
        launchWithFakePlayer(TrueFalseActivity.class);

        Espresso.onView(withId(R.id.backButton)).perform(click());
        intended(hasComponent(QuestionCreatorActivity.class.getName()));
    }

    @Test
    public void testNavigateBackFromFillBlank() throws Exception {
        launchWithFakePlayer(FillBlankActivity.class);

        Espresso.onView(withId(R.id.backButton)).perform(click());
        intended(hasComponent(QuestionCreatorActivity.class.getName()));
    }

    @Test
    public void testSaveQuestionWithCheckboxes() throws Exception {
        launchWithFakePlayer(SelectQuestionsActivity.class);

        // Input data into the EditTexts
        Espresso.onView(withId(R.id.editTextQuestion))
                .perform(ViewActions.typeText("What is the largest planet"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.editTextCorrect))
                .perform(ViewActions.typeText("Jupiter"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.editTextIncorrect1))
                .perform(ViewActions.typeText("Mars"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.editTextIncorrect2))
                .perform(ViewActions.typeText("Earth"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.editTextIncorrect3))
                .perform(ViewActions.typeText("Venus"), ViewActions.closeSoftKeyboard());

        // Interact with checkboxes
        Espresso.onView(withId(R.id.checkboxOption1)).perform(click());

        // Click the save button
        Espresso.onView(withId(R.id.buttonSave)).perform(click());

    }

    @Test
    public void testEditSelectQuestion() throws Exception {
        launchWithFakePlayer(SelectQuestionsActivity.class);

        Espresso.onView(withId(R.id.editquestionid))
                .perform(ViewActions.typeText("123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.editTextCorrect))
                .perform(ViewActions.typeText("Sun"), ViewActions.closeSoftKeyboard());

        // Click the edit button
        Espresso.onView(withId(R.id.editButton)).perform(click());
    }

    @Test
    public void testSaveTrueFalseQuestion() throws Exception {
        launchWithFakePlayer(TrueFalseActivity.class);

        // Input data into the EditTexts
        Espresso.onView(withId(R.id.editTextQuestion))
                .perform(ViewActions.typeText("Is the Earth flat"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.editTextCorrect))
                .perform(ViewActions.typeText("false"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.editTextIncorrect1))
                .perform(ViewActions.typeText("true"), ViewActions.closeSoftKeyboard());

        // Click the save button
        Espresso.onView(withId(R.id.buttonSave)).perform(click());
    }

    @Test
    public void testEditTrueFalseQuestion() throws Exception {
        launchWithFakePlayer(TrueFalseActivity.class);

        // Assume a question ID is already loaded for editing
        Espresso.onView(withId(R.id.editquestionid))
                .perform(ViewActions.typeText("123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.editTextCorrect))
                .perform(ViewActions.typeText("true"), ViewActions.closeSoftKeyboard());

        // Click the edit button
        Espresso.onView(withId(R.id.editButton)).perform(click());
    }

    @Test
    public void testSaveFillBlankQuestion() throws Exception {
        launchWithFakePlayer(FillBlankActivity.class);

        // Input data into the EditTexts
        Espresso.onView(withId(R.id.editTextQuestion))
                .perform(ViewActions.typeText("Complete the phrase: Better late than ___."), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.editTextCorrect))
                .perform(ViewActions.typeText("never"), ViewActions.closeSoftKeyboard());

        // Click the save button
        Espresso.onView(withId(R.id.buttonSave)).perform(click());
    }

    @Test
    public void testEditFillBlankQuestion() throws Exception {
        launchWithFakePlayer(FillBlankActivity.class);

        // Assume an ID is preloaded for editing
        Espresso.onView(withId(R.id.editquestionid))
                .perform(ViewActions.typeText("321"), ViewActions.closeSoftKeyboard());
        Espresso.onView(withId(R.id.editTextCorrect))
                .perform(ViewActions.typeText("always"), ViewActions.closeSoftKeyboard());

        // Click the edit button
        Espresso.onView(withId(R.id.editButton)).perform(click());
    }
}
