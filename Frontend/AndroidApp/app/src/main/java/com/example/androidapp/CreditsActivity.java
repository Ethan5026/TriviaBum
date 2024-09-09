package com.example.androidapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * This class operates and binds xml objects for the Credits Page to display
 * the developement team's information
 */
public class CreditsActivity extends AppCompatActivity {
    /**
     * The Media Player for the sick sounds
     */
    private MediaPlayer mediaPlayer;

    /**
     * Binds the back button to route back to the MainActivity page
     * on creation of the page.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        uiDesign.applyRandomBackgroundColor(this);

        Button backButton = findViewById(R.id.credits_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Sends the user back to the Main Activity
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;

                Intent intent = new Intent(CreditsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        mediaPlayer = MediaPlayer.create(CreditsActivity.this, R.raw.champions);
        mediaPlayer.start();
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
