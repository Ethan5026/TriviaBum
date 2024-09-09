package com.example.androidapp;//Types of widgets
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//For JSON Importing to check username/passwords
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * The class that binds and dictates the fucntionality of xml design elements
 * to login or signup users using Volley CRUDL requests.
 */
public class LoginSignupActivity extends AppCompatActivity {
    /**
     * The text entry box for the username for login/signup
     */
    EditText usernameEntry;
    /**
     * The text entry box for the password for login/signup
     */
    EditText passwordEntry;
    /**
     * The text box to display any Volley errors for login/signup
     */
    private static TextView errorText;
    /**
     * The string set to hold the value placed in the username text entry box
     */
    static String usernameText;
    /**
     * The string set to hold the value placed in the password text entry box
     */
    String passwordText;

    /**
     * The Volley request queue to help perform CRUDL operations
     */
    private RequestQueue requestQueue;

    private static TextView Title;


    /**
     * The creation and binding of xml design elements to manage buttons and text boxes
     * with functionality of a login/signup page.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginsignup);

        Title = (TextView) findViewById(R.id.Title_Text);

        int backgroundColor = uiDesign.applyRandomBackgroundColor(this);
        //uiDesign.loadTitleAnimation(Title, backgroundColor);

        Button loginButton = (Button) findViewById(R.id.Login_Button);
        Button signupButton = (Button) findViewById(R.id.Signup_Button);
        usernameEntry = (EditText) findViewById(R.id.Username_Entry);
        passwordEntry = (EditText) findViewById(R.id.Password_Entry);
        Button backButton = (Button) findViewById(R.id.Back_Button1);
        Button guestButton = (Button) findViewById(R.id.guestButton);
        errorText = (TextView) findViewById(R.id.Error_Text);
        requestQueue = Volley.newRequestQueue(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Attempts to login as a user with the given credentials
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                usernameText = usernameEntry.getText().toString();
                passwordText = passwordEntry.getText().toString();

                //grab the json object of the user with volley
                loginUser(requestQueue, usernameText);

            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Attempts to sign up a user with the entered credentials
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                String usernameText = usernameEntry.getText().toString();
                String passwordText = passwordEntry.getText().toString();
                //Add a new user with volley
                signupUser(requestQueue, usernameText, passwordText);

            }
        });
        backButton.setOnClickListener(new View.OnClickListener(){
            /**
             * Sends user back to the main screen
              * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginSignupActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });
        guestButton.setOnClickListener(new View.OnClickListener(){
            /**
             * Sends user to the game menu with a temporary guest account
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginSignupActivity.this, GameMenuActivity.class);
                String guestObject = "{\n" +
                        "    \"powerup\": 0," +
                        "    \"playerId\": 0,\n" +
                        "    \"playerUsername\": \"Guest\",\n" +
                        "    \"playerHashedPassword\": \"null\",\n" +
                        "    \"totalQuestions\": 0,\n" +
                        "    \"totalCorrect\": 0,\n" +
                        "    \"currentStreak\": 0,\n" +
                        "    \"longestStreak\": 0,\n" +
                        "    \"correctRatio\": 0.0,\n" +
                        "    \"friendlist\": 0,\n" +
                        "    \"admin\": false,\n" +
                        "    \"questionMaster\": false\n" +
                        "}";
                JSONObject guestJSON;
                try {
                    guestJSON = new JSONObject(guestObject);
                    Random random = new Random();
                    String randomusername = "Guest-" + (random.nextInt(90000) + 10000);
                    guestJSON.put("playerUsername",randomusername);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                intent.putExtra("playerObject", guestJSON.toString());
                intent.putExtra("isGuest", true);
                startActivity(intent);
            }

        });
    }

    /**
     * Hashes a string input with encryption
     * @param input The String you would like hashed
     * @return The hashed string of the String input
     */
    public static String hashString(String input) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // Update the digest with the input string
            md.update(input.getBytes());

            // Get the hash bytes
            byte[] hashBytes = md.digest();

            // Convert the hash bytes to a hexadecimal string
            StringBuilder hashStringBuilder = new StringBuilder();
            for (byte b : hashBytes) {
                hashStringBuilder.append(String.format("%02x", b));
            }

            return hashStringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception (e.g., print an error message)
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Performs the GET CRUDL operation that gets object of an existing user
     * @param requestQueue The volley request queue to run CRUDL operations
     * @param username The JSON player object's username with the account's username
     */
    // Make a GET request to retrieve user data
    public void loginUser(RequestQueue requestQueue, String username) {
        String url = "http://coms-309-041.class.las.iastate.edu:8080/users/" + username;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    /**
                     * On a successful GET request, check the password, if correct, display a success message, and route the user to the GameMenu page
                     * If the password is not correct, display the password was incorrect
                     * @param response The Volley returned JSONObject of the user with the username
                     */
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the JSON response
                        if(response == null){
                            errorText.setText("Username not found");
                        }
                        else{

                            try {
                                if(response.getString("playerUsername").equals(usernameText) && response.getString("playerHashedPassword").equals(hashString(passwordText))) {
                                    errorText.setText("Success");
                                    Intent intent = new Intent(LoginSignupActivity.this, GameMenuActivity.class);
                                    intent.putExtra("playerObject", response.toString());
                                    intent.putExtra("isGuest", false);
                                    startActivity(intent);
                                }
                                else{
                                    errorText.setText("Incorrect Password");
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * On an unsuccessful GET request, display the error message to the screen
                     * @param error The Volley error that caused the GET request to fail
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        errorText.setText("Could not login user");
                    }
                }
        );

        // Add the request to the provided Volley queue
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * The POST CRUDL operation that creates a new user object in the database and logs you in
     * @param requestQueue The Volley Request Queue that helps perform the CRUDL operation.
     * @param username The username of the user, found from the text entry
     * @param password The hashed password of the user, found and hashed from the text entry
     */
    public void signupUser(RequestQueue requestQueue, String username, String password){
        //url bad
        String url = "http://coms-309-041.class.las.iastate.edu:8080/users/post/" + username + '/' + hashString(password) +"/0/0";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                //json body request
                null,
                new Response.Listener<JSONObject>() {
                    /**
                     * On a successful response, display the success message and send user to the GameMenu page
                     * @param response The Volley returned JSONObject of the user
                     */
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the JSON response
                        errorText.setText("Success: New user added");
                        Intent intent = new Intent(LoginSignupActivity.this, GameMenuActivity.class);
                        intent.putExtra("playerObject", response.toString());
                        intent.putExtra("isGuest", false);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * On an unsuccessful POST request, display the error to the screen
                     * @param error The Volley error that caused a failure in the POST request
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        errorText.setText("Could not sign up user");
                    }
                }
        );

        // Add the request to the provided Volley queue
        requestQueue.add(jsonObjectRequest);
    }
}
