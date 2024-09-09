package com.example.androidapp.CarterSystemTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.androidapp.PlayActivity;
import com.example.androidapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestPlay {

    /**
     * Holds the integer of milliseconds to wait in between each volley request
     */
    private static int SIMULATION_DELAY_MS = 2000;

    @Rule
    public IntentsTestRule<PlayActivity> intentsTestRule = new IntentsTestRule<>(PlayActivity.class, true, false);

    private void launchWithFakePlayer() throws Exception {
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
                "    \"longestStreak\": 0,\n" +
                "    \"correctRatio\": 0,\n" +
                "    \"friendlist\": \"testCaseFriend\",\n" +
                "    \"admin\": false,\n" +
                "    \"questionMaster\": false\n" +
                "}");

        Intent intent = new Intent();
        intent.putExtra("isGuest", false);
        intent.putExtra("playerObject", fakePlayer.toString());

        intentsTestRule.launchActivity(intent);
    }

    @Test
    public void testHandleQuestionsResponse() throws Exception {
        launchWithFakePlayer();

        JSONArray fakeQuestions = new JSONArray();
        JSONObject question = new JSONObject();
        question.put("question", "What is the capital of France?");
        question.put("answer1", "Paris");
        question.put("answer2", "London");
        question.put("answer3", "Berlin");
        question.put("answer4", "Madrid");

        fakeQuestions.put(question);

        intentsTestRule.getActivity().runOnUiThread(() -> {
            intentsTestRule.getActivity().handleQuestionsResponse(fakeQuestions, true, false, false, false, false);
        });

        onView(ViewMatchers.withId(R.id.questionTextbox)).check(matches(isDisplayed()));
        onView(withId(R.id.choice1Button)).check(matches(isDisplayed()));
        onView(withId(R.id.choice2Button)).check(matches(isDisplayed()));
        onView(withId(R.id.choice3Button)).check(matches(isDisplayed()));
        onView(withId(R.id.choice4Button)).check(matches(isDisplayed()));

        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}
    }

    @Test
    public void testLoadingFillInTheBlankQuestion() throws Exception {
        launchWithFakePlayer();

        intentsTestRule.getActivity().runOnUiThread(() -> intentsTestRule.getActivity().updateUIWithfillBlankQuestion(createFakeFillInBlankQuestion()));

        onView(withId(R.id.questionTextbox)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextFillBlank)).check(matches(isDisplayed()));
        onView(withId(R.id.enterButton)).check(matches(isDisplayed()));
        onView(withId(R.id.choice1Button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.choice2Button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.choice3Button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.choice4Button)).check(matches(not(isDisplayed())));

        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}
    }

    private JSONObject createFakeFillInBlankQuestion() {
        try {
            JSONObject question = new JSONObject();
            question.put("question", "What is the capital of France?");
            question.put("correctAnswer", "Paris");
            return question;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testLoadingTrueFalseQuestion() throws Exception {

        launchWithFakePlayer();

        intentsTestRule.getActivity().runOnUiThread(() -> intentsTestRule.getActivity().updateUIWithTrueFalseQuestion(createFakeTrueFalseQuestion()));

        onView(withId(R.id.questionTextbox)).check(matches(isDisplayed()));
        onView(withId(R.id.choice1Button)).check(matches(isDisplayed()));
        onView(withId(R.id.choice2Button)).check(matches(isDisplayed()));
        onView(withId(R.id.choice3Button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.choice4Button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.editTextFillBlank)).check(matches(not(isDisplayed())));
        onView(withId(R.id.enterButton)).check(matches(not(isDisplayed())));

        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}
    }

    private JSONObject createFakeTrueFalseQuestion() {
        try {
            JSONObject question = new JSONObject();
            question.put("question", "The sky is blue.");
            question.put("correctAnswer", true);
            return question;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testLoadingMultipleChoiceQuestion() throws Exception {
        launchWithFakePlayer();

        intentsTestRule.getActivity().runOnUiThread(() -> intentsTestRule.getActivity().updateUIWithMultipleChoiceQuestion(createFakeMultipleChoiceQuestion()));

        onView(withId(R.id.questionTextbox)).check(matches(isDisplayed()));
        onView(withId(R.id.choice1Button)).check(matches(isDisplayed()));
        onView(withId(R.id.choice2Button)).check(matches(isDisplayed()));
        onView(withId(R.id.choice3Button)).check(matches(isDisplayed()));
        onView(withId(R.id.choice4Button)).check(matches(isDisplayed()));
        onView(withId(R.id.editTextFillBlank)).check(matches(not(isDisplayed())));
        onView(withId(R.id.enterButton)).check(matches(not(isDisplayed())));

        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}
    }

    @Test
    public void testLoadingSelectQuestion() throws Exception {
        launchWithFakePlayer();

        intentsTestRule.getActivity().runOnUiThread(() -> intentsTestRule.getActivity().updateUIWithSelectQuestion(createFakeSelectQuestion()));

        onView(withId(R.id.questionTextbox)).check(matches(isDisplayed()));
        onView(withId(R.id.checkboxOption1)).check(matches(isDisplayed()));
        onView(withId(R.id.checkboxOption2)).check(matches(isDisplayed()));
        onView(withId(R.id.checkboxOption3)).check(matches(isDisplayed()));
        onView(withId(R.id.checkboxOption4)).check(matches(isDisplayed()));
        onView(withId(R.id.submitBtn)).check(matches(isDisplayed()));
        onView(withId(R.id.choice1Button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.choice2Button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.choice3Button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.choice4Button)).check(matches(not(isDisplayed())));
        onView(withId(R.id.editTextFillBlank)).check(matches(not(isDisplayed())));
        onView(withId(R.id.enterButton)).check(matches(not(isDisplayed())));

        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}
    }

    public JSONObject createFakeSelectQuestion() {
        try {
            JSONObject selectquestion = new JSONObject();
            selectquestion.put("question", "Select all prime numbers:");
            selectquestion.put("answer1", "2");
            selectquestion.put("answer1correct", true);
            selectquestion.put("answer2", "3");
            selectquestion.put("answer2correct", true);
            selectquestion.put("answer3", "4");
            selectquestion.put("answer3correct", false);
            selectquestion.put("answer4", "5");
            selectquestion.put("answer4correct", true);
            return selectquestion;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void TestWebsockets() throws Exception{
        launchWithFakePlayer();

        int fakescore = 1000;

        onView(withId(R.id.ScoreBox));

        intentsTestRule.getActivity().runOnUiThread(() -> intentsTestRule.getActivity().onWebSocketScore(fakescore));

        onView(withId(R.id.ScoreBox)).check(matches(withText("Score:"+fakescore)));

        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}
    }

    @Test
    public void TestEndActivity() throws Exception{
        launchWithFakePlayer();

        intentsTestRule.getActivity().endActivity();

        try {
            Thread.sleep(SIMULATION_DELAY_MS);
        } catch (InterruptedException ignored) {}
    }

    @Test
    public void TestIncorrectAnswer() throws Exception {
        launchWithFakePlayer();

        // Assuming updateUIWithMultipleChoiceQuestion loads questions and sets the correct answer on choice1Button
        intentsTestRule.getActivity().runOnUiThread(() -> {

            fakeQuestion = createFakeMultipleChoiceQuestion();

            intentsTestRule.getActivity().updateUIWithMultipleChoiceQuestion(fakeQuestion);

        });

        // Add a delay to allow UI updates to process if necessary
        Thread.sleep(SIMULATION_DELAY_MS);

        Espresso.onView(withText(fakeQuestion.getString("answer2"))).perform(click());
    }

    public JSONObject fakeQuestion;

    public JSONObject createFakeMultipleChoiceQuestion() {
        try {
            JSONObject question = new JSONObject();
            question.put("question", "What is 2 + 2?");
            question.put("answer1", "4");
            question.put("answer2", "3");
            question.put("answer3", "2");
            question.put("answer4", "5");
            return question;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void TestCorrectAnswer() throws Exception {
        launchWithFakePlayer();

        // Assuming updateUIWithMultipleChoiceQuestion loads questions and sets the correct answer on choice1Button
        intentsTestRule.getActivity().runOnUiThread(() -> {

            fakeQuestion  = createFakeMultipleChoiceQuestion();

            intentsTestRule.getActivity().updateUIWithMultipleChoiceQuestion(fakeQuestion);

        });

        // Add a delay to allow UI updates to process if necessary
        Thread.sleep(SIMULATION_DELAY_MS);

        Espresso.onView(withText(fakeQuestion.getString("answer1"))).perform(click());
    }

}
