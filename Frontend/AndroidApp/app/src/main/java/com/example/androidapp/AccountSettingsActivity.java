package com.example.androidapp;//Types of widgets
import static com.android.volley.Request.Method.GET;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *  Class that monitors and controls a screen process where users can access their account information
 *  such as usernames, passwords, and organizations. A series of buttons and text entries dictates updating
 *  user information in the backend through REST functions.
 */
public class AccountSettingsActivity extends AppCompatActivity{


    /**
     * Text box used to display a user's username within the account settings
     */
    public TextView usernameTitle;

    /**
     * The text box used to display a user's organization key within the account settings page
     */
    public static TextView orgTitle;
    /**
     * The text box used to display error messages when updating account info
     */
    public static TextView usernameError;
    /**
     * The text box used to display error messages when account password info
     */
    public TextView passwordError;
    /**
     * The text box used to display error messages when updating account organization info
     */
    public static TextView orgError;

    /**
     * The JSON object of the user object containing its account info
     */
    private JSONObject playerObject;

    /**
     * The text entry box for the user to input their desired new username
     */
    private EditText usernameTextEntry;
    /**
     * The text entry box for the user to input their desired new password
     */
    private EditText passwordTextEntry;
    /**
     * The text entry box for the user to input their desired new password again for verification
     */
    private EditText passwordTextEntry2;
    /**
     * The text entry box for the user to input their desired new organization ID
     */
    private EditText orgTextEntry;

    /**
     * The String value to set the text that was entered into the username text entry
     */
    private String usernameText;
    /**
     * The String value to set the text that was entered into the password text entry
     */
    private String passwordText;
    /**
     * The volley request Queue to make new Volley CRUDL operations
     */
    private RequestQueue requestQueue;

    /**
     * The string holding the entered organization code
     */
    private String newUserOrg;


    /**
     * The function that creates and binds xml objects with functionalities for the account
     * settings page
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        uiDesign.applyRandomBackgroundColor(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsettings);
        // Retrieve the JSONObject from the Intent
        Intent intent = getIntent();
        if (intent.hasExtra("playerObject")) {
            String jsonString = intent.getStringExtra("playerObject");

            // Convert the String back to JSONObject
            try {
                assert jsonString != null;
                playerObject = new JSONObject(jsonString);

                // Now you can use the receivedJsonObject as needed

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        requestQueue = Volley.newRequestQueue(this);
        Button backButton = (Button) findViewById(R.id.backButton);
        usernameTitle = (TextView) findViewById(R.id.usernameChangeText);
        usernameTextEntry = (EditText) findViewById(R.id.usernameChangeTextEntry);
        Button usernameButton = (Button) findViewById(R.id.usernameChangeButton);
        passwordTextEntry = (EditText) findViewById(R.id.passwordChangeTextEntry);
        passwordTextEntry2 = (EditText) findViewById(R.id.passwordChangeTextEntry2);
        Button passwordButton = (Button) findViewById(R.id.passwordChangeButton);
        orgTitle = (TextView) findViewById(R.id.orgChangeText);
        orgTextEntry = (EditText) findViewById(R.id.orgChangeTextEntry);
        Button orgChangeButton = (Button) findViewById(R.id.orgChangeButton);
        Button orgLeaveButton = (Button) findViewById(R.id.orgLeaveButton);
        usernameError = (TextView) findViewById(R.id.usernameChangeResponseText);
        passwordError = (TextView) findViewById(R.id.passwordChangeResponseText);
        orgError = (TextView) findViewById(R.id.orgChangeResponseText);
        Button deleteButton = (Button) findViewById(R.id.deleteButton);

        try {
            String text = "Username: " + playerObject.getString("playerUsername");
            usernameTitle.setText(text);
        } catch (JSONException e) {
            usernameTitle.setText("Username broken");
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Sends the user back to the Game Menu with its information
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountSettingsActivity.this, GameMenuActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                startActivity(intent);
            }
        });
        usernameButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Attempts to edit the username
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                usernameText = usernameTextEntry.getText().toString();
                try {
                    playerObject.put("playerUsername", usernameText);
                } catch (JSONException e) {
                    usernameError.setText("Cannot submit new username");
                }

                AccountSettingsActivity.VolleyCallback callback = new VolleyCallback() {
                    /**
                     * Displays the successful operation response in the username textbox
                     * @param result The resulting JSON object from the operation
                     */
                    @Override
                    public void onSuccess(JSONObject result) {
                        String text = "Success! Changed to " + usernameText;
                        usernameError.setText(text);
                        text = "Username: " + usernameText;
                        usernameTitle.setText(text);
                    }

                    /**
                     * Display the error message in the user text box from the Volley query
                     * @param error The Volley String error
                     */
                    @Override
                    public void onError(String error) {
                        usernameError.setText(error);
                        ;
                    }
                };
                try {
                    editUser(requestQueue, playerObject, callback);
                } catch (JSONException e) {
                    usernameError.setText(e.toString());
                }

            }
        });
        passwordButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Attempts to change the password
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                if (passwordTextEntry.getText().toString().equals(passwordTextEntry2.getText().toString())) {
                    passwordText = passwordTextEntry.getText().toString();
                    try {
                        playerObject.put("playerHashedPassword", hashString(passwordText));
                    } catch (JSONException e) {
                        passwordError.setText("Cannot submit new password");
                    }
                    AccountSettingsActivity.VolleyCallback callback = new VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            String text = "Success! Password Changed";
                            passwordError.setText(text);
                        }

                        @Override
                        public void onError(String error) {
                            passwordError.setText(error);
                        }
                    };
                    try {
                        editUser(requestQueue, playerObject, callback);
                    } catch (JSONException e) {
                        passwordError.setText(e.toString());
                    }

                } else {
                    passwordError.setText("Passwords do not match");
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Attempts to delete the user
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                try {
                    deleteUser(requestQueue, playerObject);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        orgChangeButton.setOnClickListener(new View.OnClickListener(){
            /**
             * Attempts to change the user's organization
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                newUserOrg = orgTextEntry.getText().toString();
                try {
                    setOrg(requestQueue, playerObject, newUserOrg);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        orgLeaveButton.setOnClickListener(new View.OnClickListener(){
            /**
             * Attempts to leave the user's organization
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                AccountSettingsActivity.VolleyCallback callback = new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        String text = "Success! Changed to default clan";
                        orgError.setText(text);
                    }

                    @Override
                    public void onError(String error) {
                        orgError.setText(error);

                    }
                };
                try {

                    setOrg(requestQueue, playerObject, "2002");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                orgTitle.setText("Clan: default");

            }
        });
        //Edit the Org Title
        try {
            getOrg(playerObject);
        } catch (JSONException e) {
            orgError.setText("Could not get Clan");
        }

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
     * Performs a PUT operation, editing a user in the database
     * @param requestQueue The volley request queue to run CRUDL operations
     * @param player The JSON player object with the account info
     * @param callback The function to return to after editing the user
     * @throws JSONException Throws if a user object or its expected attributes aren't found
     */
    public static void editUser(RequestQueue requestQueue, JSONObject player, final VolleyCallback callback) throws JSONException {
        String url = "http://coms-309-041.class.las.iastate.edu:8080/users/";
        int id = player.getInt("playerId");
        url += id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                player,

                new Response.Listener<JSONObject>() {
                    /**
                     * Calls the function with the JSON Object as a parameter to respond for the appropriate edit
                     * @param response The Volley JSON object response from the PUT request
                     */
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the JSON response
                        try {
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            usernameError.setText(e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * Calls the error function to display the error message for the appropriate change
                     * @param error The Volley error that caused the PUT request to fail
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        try {
                            callback.onError(error.toString());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );

        // Add the request to the provided Volley queue
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Completes the DELETE operation for a user, deleting them from the database,
     * and exits to the title page
     * @param requestQueue The volley request queue to run CRUDL operations
     * @param player The JSON player object with the account info
     * @throws JSONException Throws if a user object or its expected attributes aren't found

     */
    public void deleteUser(RequestQueue requestQueue, JSONObject player) throws JSONException {
        String url = "http://coms-309-041.class.las.iastate.edu:8080/users/";
        int id = player.getInt("playerId");
        url += id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                player,
                new Response.Listener<JSONObject>() {
                    /**
                     * On a successful deletion of a user, the user is routed back to the login/signup screen
                     * @param response The JSON Object returned from the DELETE request
                     */
                    @Override
                    public void onResponse(JSONObject response) {

                        Intent intent = new Intent(AccountSettingsActivity.this, LoginSignupActivity.class);
                        startActivity(intent);
                    }
                },

                new Response.ErrorListener() {
                    /**
                     * On an unsuccessful deletion of a user, the user is shown the error in the error textbox
                     * @param error The Volley error causing the unsuccessful DELETE request
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        usernameError.setText(error.toString());
                    }
                }
        );

        // Add the request to the provided Volley queue
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Performs the GET CRUDL operation that gets the organization name of a user
     * * @param player The JSON player object with the account info
     * @throws JSONException Throws if a user object or its expected attributes aren't found

     */
    public void getOrg(JSONObject player) throws JSONException {
        String url = "http://coms-309-041.class.las.iastate.edu:8080/userorg/";
        String username = player.getString("playerUsername");
        url += username;

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    /**
                     * On a successful GET request, the organization text display is updated to show the user's organization
                     * @param response The String of the user's organization from the GET request
                     */
                    @Override
                    public void onResponse(String response) {
                        orgTitle.setText("Clan: " + response);
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
                        orgError.setText("Error getting clan");
                    }
                }
        );

        // Add the request to the provided Volley queue
        requestQueue.add(stringRequest);
    }

    /**
     * Completes a PUT request to edit the organization of a user
     * @param requestQueue The volley request queue to run CRUDL operations
     * @param player The JSON player object with the account info
     * @param newUserOrg The desired new Organization ID for the user
     * @throws JSONException Throws if a user object or its expected attributes aren't found
     */
    public void setOrg(RequestQueue requestQueue, JSONObject player, String newUserOrg) throws JSONException {
        String url = "http://coms-309-041.class.las.iastate.edu:8080/userorg/";
        int id = player.getInt("playerId");
        url += "" + id + '/' + newUserOrg;

        JsonObjectRequest stringRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    /**
                     * On a successful PUT request, the users new organization is displayed
                     * @param response The object of the user's new organization from the PUT request
                     */
                    @Override
                    public void onResponse(JSONObject response) {
                        playerObject = response;
                        try {
                            getOrg(response);
                        } catch (JSONException e) {
                            orgError.setText("Could not change clan");
                        }
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * On an unsuccessful PUT request, the Volley Error is displayed to the screen
                     * @param error The Volley error causing the request to fail
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        orgError.setText("Could not change clan");
                    }
                }
        );

        // Add the request to the provided Volley queue
        requestQueue.add(stringRequest);
    }

    /**
     * A defined callback response function to operate specific instructions based
     * on a success or a failure.
     */
    public interface VolleyCallback {
        /**
         * Operates when a successful Volley CRUDL operation was performed
         * @param result The resulting JSON object from the operation
         * @throws JSONException An exception is thrown when the user's object or expected attributes aren't found
         */
        void onSuccess(JSONObject result) throws JSONException;

        /**
         * Operates when an unsuccessful Volley CRUDL operation was performed
         * @param error The Volley String error
         * @throws JSONException An exception is thrown when the user's object or expected attributes aren't found
         */
        void onError(String error) throws JSONException;
    }
}
