package com.example.androidapp;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class uiDesign {

    private static final int[] COLORS = new int[]{
            R.color.Baby_Blue,
            R.color.Cornflower_Blue,
            R.color.Dodger_Blue,
            R.color.Blue_Crayola,
            R.color.Blue_Munsell,
            R.color.Blue_NCS,
            R.color.Blue_Pigment,
            R.color.Royal_Blue,
            R.color.Midnight_Blue,
            R.color.Prussian_Blue,
            R.color.Space_Blue,
            R.color.Indigo_Dye,
            R.color.Yale_Blue,
            R.color.Egyptian_Blue
    };

    public static int applyRandomBackgroundColor(Activity activity) {
        // Get the root view
        View rootView = activity.findViewById(android.R.id.content);

        // Generate a random index
        int randomColorIndex = new Random().nextInt(COLORS.length);

        // Get the actual color value
        int color = activity.getResources().getColor(COLORS[randomColorIndex], activity.getTheme());

        // Set the background color to a random color
        rootView.setBackgroundColor(color);

        return color;
    }

    private static void adjustViewColors(View view, int backgroundColor) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                adjustViewColors(viewGroup.getChildAt(i), backgroundColor);
            }
        } else if (view instanceof TextView) { // Adjusting text color
            TextView textView = (TextView) view;
            textView.setTextColor(isColorDark(backgroundColor) ? Color.WHITE : Color.BLACK);
        }

        if (view instanceof Button) { // Adjusting button background and text color
            Button button = (Button) view;
            button.setTextColor(isColorDark(backgroundColor) ? Color.BLACK : Color.WHITE); // Invert text color relative to button background
            button.setBackgroundColor(isColorDark(backgroundColor) ? Color.LTGRAY : Color.GRAY); // Adjust button background
        }
    }

    // Utility method to check if the color is dark
    private static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return darkness >= 0.5;
    }

    public static int[] titleColors = new int[]{
            Color.WHITE, // Good against dark backgrounds
            Color.BLACK, // Good against light backgrounds
            Color.YELLOW, // Usually stands out
            Color.RED, // Bold and generally visible
            Color.BLUE  // Bright and clear
    };


    /**
     * makes a title animation in the Main Activity
     */
    public static void loadTitleAnimation(TextView titleTextView, int backgroundColor) {
        // Initial text color based on background color for contrast
        titleTextView.setTextColor(isColorDark(backgroundColor) ? Color.WHITE : Color.BLACK);

        // Create a ValueAnimator instance
        ValueAnimator colorAnimation = ValueAnimator.ofArgb(Color.WHITE, Color.BLACK, Color.YELLOW, Color.RED, Color.BLUE);
        colorAnimation.setDuration(5000); // Set the duration to cycle through the colors
        colorAnimation.setRepeatCount(ValueAnimator.INFINITE); // Set it to repeat indefinitely
        colorAnimation.setRepeatMode(ValueAnimator.RESTART);

        // Update the TextView's text color with the animated color value
        colorAnimation.addUpdateListener(animation -> {
            int animatedColor = (int) animation.getAnimatedValue();
            titleTextView.setTextColor(animatedColor);
        });

        colorAnimation.start();
    }

    /**
     * floating text that says thanks for playing
     * @param activity
     * @param textView
     * @param backgroundColor
     */
    public static void loadFloatingTextAnimation(Activity activity, TextView textView, int backgroundColor) {
        // Position animation setup
        float startY = textView.getY();
        float endY = startY - 200; // Change 200 to the distance you want the text to move up

        ValueAnimator positionAnimator = ValueAnimator.ofFloat(startY, endY);
        positionAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        positionAnimator.setDuration(3000); // Duration of 3 seconds
        positionAnimator.setRepeatCount(ValueAnimator.INFINITE);
        positionAnimator.setRepeatMode(ValueAnimator.REVERSE);
        positionAnimator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            textView.setY(animatedValue);
        });

        // Color animation setup, similar to title color change
        int[] textColors = new int[]{0xFFFFFFFF, 0xFF000000, 0xFFFFFF00, 0xFFFF0000, 0xFF0000FF}; // White, Black, Yellow, Red, Blue
        ValueAnimator colorAnimator = ValueAnimator.ofArgb(textColors);
        colorAnimator.setDuration(5000); // Duration of 5 seconds to cycle through the colors
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator.setRepeatMode(ValueAnimator.RESTART);
        colorAnimator.addUpdateListener(animation -> {
            int animatedColor = (int) animation.getAnimatedValue();
            textView.setTextColor(animatedColor);
        });

        // Start both animations
        positionAnimator.start();
        colorAnimator.start();
    }

}
