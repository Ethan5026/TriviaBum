package com.example.androidapp;//Types of widgets
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.widget.ImageView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;


import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

/**
 * The class linking xlm elements to display a user's profile using
 * the user's profile object
 */
public class ProfileActivity extends AppCompatActivity{

    /**
     * The JSON object of a user, containing its stats information.
     */
    private JSONObject playerObject;

    /**
     * The JSON object of the user to display its stats information.
     */
    private JSONObject profileObject;

    /**
     * The request queue for volley get request
     */
    private RequestQueue requestQueue;
    /**
     * Where to route to when pressing the back button. 0 = GameMenu, 1 = Clan Page
     */
    private int backRouter;
    /**
     * Holds the error or success messages during volley requests
     */
    private TextView errorBox;

    /**
     * The string name of the clan of the profile's user.
     */
    private String clanName;

    /**
     * Holds the string array of all the user's friends
     */
    private String[] friends;

    /**
     * The image element from the xml file to hold the profile image
     */
    private ImageView imageView;

    /**
     * Stores the username of the profile
     */
    private String profileUsername;


    /**
     * The overidden function to create the android screen
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        uiDesign.applyRandomBackgroundColor(this);

        // Retrieve the JSONObject from the Intent
        Intent intent = getIntent();
        String picture_url = null;
        String bio = null;
        String name = null;
        String username = null;
        clanName = null;
        imageView = findViewById(R.id.profile_image);
        Button backButton = (Button) findViewById(R.id.backButton);
        Button editProfile = (Button) findViewById(R.id.editProfile);
        requestQueue = Volley.newRequestQueue(this);
        errorBox = (TextView) findViewById(R.id.errorText);

        if (intent.hasExtra("playerObject")) {
            String jsonString = intent.getStringExtra("playerObject");
            profileUsername = intent.getStringExtra("profileUsername");
            backRouter = intent.getIntExtra("routeTo", 0);

            // Convert the String back to JSONObject
            try {
                assert jsonString != null;
                playerObject = new JSONObject(jsonString);
                username = playerObject.getString("playerUsername");
                assert profileUsername != null;
                if (!profileUsername.equals(username)) {
                    editProfile.setVisibility(View.INVISIBLE);
                    editProfile.setClickable(false);
                }

                // Make a GET request using Volley to fetch the list of friends
                StringRequest stringRequest = new StringRequest(
                        Request.Method.GET,
                        "http://coms-309-041.class.las.iastate.edu:8080/friends/" + profileUsername,
                        new Response.Listener<String>() {
                            /**
                             * Handle the response containing the string of friends.
                             *
                             * @param response The string response containing the list of friends.
                             */
                            @Override
                            public void onResponse(String response) {
                                // Split the string response into an array of friend usernames
                                friends = response.split(" ");
                                String friendsString = Arrays.toString(friends);
                                if(friendsString.length() > 2){
                                    friendsString = friendsString.substring(1, friendsString.length()-1);
                                }
                                ((TextView)findViewById(R.id.friendsText)).setText(friendsString);
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
                                errorBox.setText("Could not load user's friends");
                            }
                        }
                );
                requestQueue.add(stringRequest);

                //Get the profile object
                JsonObjectRequest profileRequest = new JsonObjectRequest(
                        "http://coms-309-041.class.las.iastate.edu:8080/profile/" + profileUsername,
                        new Response.Listener<JSONObject>() {
                            /**
                             * Sets the instance variable of the profile Object on a valid Volley response
                             *
                             * @param response the profile object of the given player
                             */
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    profileObject = new JSONObject(response.toString());
                                    String picture_url = response.getString("profileImageUrl");
                                    setProfileImage(picture_url);

                                    String bio = response.getString("userBio");
                                    ((TextView) findViewById(R.id.bioText)).setText(bio);

                                    String name = response.getString("fullName");
                                    ((TextView) findViewById(R.id.main_msg_txt)).setText(name);
                                }
                                catch(JSONException e){
                                    errorBox.setText("Unable to retrieve user's profile");
                                }
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

                //Get the clan name
                StringRequest clanRequest = new StringRequest(
                        Request.Method.GET,
                        "http://coms-309-041.class.las.iastate.edu:8080/userorg/" + profileUsername,
                        new Response.Listener<String>() {
                            /**
                             * On a successful GET request, the organization text display is updated to show the user's clan
                             * @param response The String of the user's clan from the GET request
                             */
                            @Override
                            public void onResponse(String response) {
                                ((TextView)findViewById(R.id.clanText)).setText(response);
                            }
                        },
                        new Response.ErrorListener() {
                            /**
                             * On an unsuccessful GET request, the error is displayed to the text box
                             * @param error The Volley error that caused an unsuccessful GET request
                             */
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle errors
                                errorBox.setText("Error getting user's clan");
                            }
                        }
                );

                // Add the request to the provided Volley queue
                requestQueue.add(clanRequest);

            } catch (JSONException e) {
                errorBox.setText("Unable to retrieve profile");
            }
        }


        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Sends the user back to the Game Menu or Clan page with the with the player object
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (backRouter == 0) {
                    intent = new Intent(ProfileActivity.this, GameMenuActivity.class);
                } else {
                    intent = new Intent(ProfileActivity.this, ClansActivity.class);
                }
                intent.putExtra("playerObject", playerObject.toString());
                startActivity(intent);
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            /**
             * Sends the user to the edit profile page
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                startActivity(intent);
            }
        });
    }
    /**
     * Retrieve an image from Volley using a image address link
     */
    public void setProfileImage(String url){
        ImageRequest imageRequest = new ImageRequest(
                url,
                new Response.Listener<Bitmap>() {
                    /**
                     * The method running after a successful volley response to set the image into the app page
                     * @param response The bitmap for the profile image
                     */
            @Override
            public void onResponse(Bitmap response) {
                // Set the Bitmap to the ImageView
                imageView.setImageBitmap(response);
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
}
