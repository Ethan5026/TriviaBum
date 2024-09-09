package com.example.androidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Class that implements the title screen and a flow to all the other activities.
 */
public class MainActivity extends AppCompatActivity {

    private RelativeLayout mainLayout;
    private Random random = new Random();

    /**
     * button to start login signup page
     */
    private Button playButton;

    /**
     * button to start credits page
     */
    private Button creditsButton;

    /**
     * MediaPLayer for intro music
     */
    private MediaPlayer mediaPlayer;

    /**
     * Makes page layout
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.mainLayout);
        TextView titleTextView = findViewById(R.id.main_msg_txt);

        playButton = findViewById(R.id.play_Button);
        creditsButton = findViewById(R.id.credits_Button);

        TextView floatingTextView = findViewById(R.id.floating_text_view);

        // Example to apply a random background color and get the color
        int backgroundColor = uiDesign.applyRandomBackgroundColor(this); // This sets the background color
        uiDesign.loadFloatingTextAnimation(this, floatingTextView, backgroundColor);
        uiDesign.loadTitleAnimation(titleTextView, backgroundColor);

        // Convert the color to hex string
        String hexColor = colorToHexString(backgroundColor);

        // Show the color in a toast message
        //Toast.makeText(MainActivity.this, "Background color: " + hexColor, Toast.LENGTH_SHORT).show();

        /**
         * loads login signup activity
         */
        playButton.setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

            Intent intent = new Intent(MainActivity.this, LoginSignupActivity.class);
            startActivity(intent);
        });

        /**
         * loads credits activity
         */
        creditsButton.setOnClickListener(v -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

            Intent intent = new Intent(MainActivity.this, CreditsActivity.class);
            startActivity(intent);
        });

        mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.intro);
        mediaPlayer.start();
    }

    private static String colorToHexString(int color) {
        return String.format("#%06X", (0xFFFFFF & color));
    }

    /**
     * Stops the music from playing
     */
    @Override
    public void onDestroy() {
        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

        }
        super.onDestroy();
    }
}
