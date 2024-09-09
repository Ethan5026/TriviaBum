package com.example.androidapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * The class handling the screen operations for the edit profile page
 */
public class EditProfileActivity extends AppCompatActivity {
    /**
     * The object of the user
     */
    private JSONObject playerObject;

    /**
     * The object of the profile
     */
    private JSONObject profileObject;

    /**
     * Where you display errors by text on the screen
     */
    private TextView errorBox;
    /**
     * The username of the player
     */
    private String username;

    /**
     * The request queue for volley operations
     */
    private RequestQueue requestQueue;
    /**
     * The creation and initialization of buttons and displays
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        String jsonString = "";
        String jsonString1 = "";


        Intent intent = getIntent();
        uiDesign.applyRandomBackgroundColor(this);
        if (intent.hasExtra("playerObject")) {
            jsonString = intent.getStringExtra("playerObject");
        }
        try {
            playerObject = new JSONObject(jsonString);
            username = playerObject.getString("playerUsername");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Button myProfileButton = findViewById(R.id.myProfile);
        Button backButton = findViewById(R.id.backButton);
        Button setBio = findViewById(R.id.addBio);
        Button setName = findViewById(R.id.addName);
        Button setImage = findViewById(R.id.addImage);
        Button viewImage = findViewById(R.id.viewImage);
        errorBox = findViewById(R.id.errorText);
        requestQueue = Volley.newRequestQueue(this);

        //Get the profile object
        JsonObjectRequest profileRequest = new JsonObjectRequest(
                "http://coms-309-041.class.las.iastate.edu:8080/profile/" + username,
                new Response.Listener<JSONObject>() {
                    /**
                     * Sets the instance variable of the profile Object on a valid Volley response
                     *
                     * @param response the profile object of the given player
                     */
                    @Override
                    public void onResponse(JSONObject response) {
                        profileObject = response;
                    }
                },
                /**
                 * Displays an error that the player could not be updated
                 */
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorBox.setText("Unable to retrieve user's profile");
                    }
                }
        );
        requestQueue.add(profileRequest);

        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Sends the user back to the Game Menu
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, GameMenuActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                startActivity(intent);
            }
        });
        setBio.setOnClickListener(new View.OnClickListener() {
            /**
             * Edits the profile and sets it
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                try {
                    profileObject.put("userBio", ((EditText)findViewById(R.id.bioText)).getText().toString());
                } catch (JSONException e) {
                    errorBox.setText("Could not change bio");
                }
                editProfile(profileObject, username);
            }
        });
        setName.setOnClickListener(new View.OnClickListener() {
            /**
             * Edits the profile and sets it
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                try {
                    profileObject.put("fullName", ((EditText)findViewById(R.id.nameText)).getText().toString());
                } catch (JSONException e) {
                    errorBox.setText("Could not change name");
                }
                editProfile(profileObject, username);
            }
        });

        setImage.setOnClickListener(new View.OnClickListener() {
            /**
             * Edits the profile and sets it
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                try {
                    profileObject.put("profileImageUrl", ((EditText)findViewById(R.id.imageText)).getText().toString());
                } catch (JSONException e) {
                    errorBox.setText("Could not change profile image");
                }
                editProfile(profileObject, username);
            }
        });

        viewImage.setOnClickListener(new View.OnClickListener() {
            /**
             * View an image by an image address
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                ImageRequest imageRequest = new ImageRequest(
                        ((EditText)findViewById(R.id.imageText)).getText().toString(),
                        new Response.Listener<Bitmap>() {
                            /**
                             * The method running after a successful volley response to set the image into the app page
                             * @param response The bitmap for the profile image
                             */
                            @Override
                            public void onResponse(Bitmap response) {
                                // Set the Bitmap to the ImageView
                                ((ImageView)findViewById(R.id.profile_image)).setImageBitmap(response);
                            }
                        }, 350, 400, ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565,
                        new Response.ErrorListener() {
                            /**
                             * The method to run on failure to show an error message
                             * @param error The volley error for the request
                             */
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle errors
                                errorBox.setText("Could not load profile picture");
                            }
                        }
                );
                requestQueue.add(imageRequest);
            }
        });


        myProfileButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Sends the user back to the Game Menu
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    intent.putExtra("playerObject", playerObject.toString());
                    intent.putExtra("profileUsername", playerObject.getString("playerUsername"));
                    intent.putExtra("routeTo", 0);
                    startActivity(intent);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });
    }
    public void editProfile(JSONObject profile, String username){
        JsonObjectRequest profileUpdate = new JsonObjectRequest(
                Request.Method.PUT,
                "http://coms-309-041.class.las.iastate.edu:8080/profile/" + username,
                profile,
                new Response.Listener<JSONObject>() {
                    /**
                     * Handle the response containing the string of friends.
                     *
                     * @param response The string response containing the list of friends.
                     */
                    @Override
                    public void onResponse(JSONObject response) {
                        // Split the string response into an array of friend usernames
                        profileObject = response;
                        errorBox.setText("Profile editted");

                    }
                },
                new Response.ErrorListener() {
                    /**
                     * Handle errors that occur during the GET request.
                     *
                     * @param error The error object containing details about the error.
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorBox.setText("Could not edit profile");
                    }
                }
        );
        requestQueue.add(profileUpdate);
    }
}
