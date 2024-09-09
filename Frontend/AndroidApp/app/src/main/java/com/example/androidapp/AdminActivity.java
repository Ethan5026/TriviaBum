package com.example.androidapp;

import static com.example.androidapp.LoginSignupActivity.hashString;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


/**
 * Class that implements the admin screen allowing them to fix different aspects of the game and control data.
 */
public class AdminActivity extends AppCompatActivity {

    /**
     * Holds the value of the JSON user object and their info
     */
    private JSONObject playerObject;

    /**
     * Holds true if the user has admin priviledges
     */
    private boolean isAdmin;
    /**
     * Holds true if the user has question creator privledges
     */
    private boolean isQuestionCreator;

    /**
     * The text entry box for a user's username when admin is inspecting users
     */
    private EditText usernameEntry;
    /**
     * The text entry box for a user's password when admin is inspecting users
     */
    private EditText passwordEntry;
    /**
     * The text entry box for changing a user's username when admin is inspecting users
     */
    private EditText usernameChangeEntry;
    /**
     * The text entry box for changing a user's password when admin is inspecting users
     */
    private EditText passwordChangeEntry;
    /**
     * The text entry box for a user's clan code when admin is inspecting users
     */
    private EditText orgCodeEntry;

    /**
     * The text entry box for a clan's name when admin is inspecting clans
     */
    private EditText orgNameEntry;
    /**
     * The text entry box for a new clan's name when admin is inspecting clans
     */
    private EditText neworgNameEntry;

    /**
     * Checkbox to select whether a new user will or will not be an admin
     */
    private CheckBox newUserAdmin;
    /**
     * Checkbox to select whether a new user will or will not be a question creator
     */

    private CheckBox newUserQuestionCreator;

    /**
     * Checkbox to select whether a user is or is not an admin
     */
    private CheckBox isAdminCheck;
    /**
     * Checkbox to select whether a user is or is not a question creator
     */
    private CheckBox isQuestionCreatorCheck;

    /**
     * Volley requeust queue to use to make volley requests for CRUDL operations
     */
    private RequestQueue requestQueue;

    /**
     * The ID of the selected option from the dropdown menu
     */
    private int configurationID;

    /**
     * Holds the JSON array of the clans and the users to select from
     */
    private JSONArray objects;

    /**
     * The saved names of the users and organizations to place into the dropdowns
     */
    private String[] names;

    /**
     * The spinner for selecting the user and clans to edit
     */
    private Spinner spinner;

    /**
     * Text to display responses to volley
     */
    private TextView responseText;


    /**
     * loads the activity and view from the xml file and manifest.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        uiDesign.applyRandomBackgroundColor(this);




        super.onCreate(savedInstanceState);
        // Retrieve the JSONObject from the Intent
        Intent intent = getIntent();
        requestQueue = Volley.newRequestQueue(this);
        if (intent.hasExtra("playerObject")) {
            String jsonString = intent.getStringExtra("playerObject");

            // Convert the String back to JSONObject
            try {
                assert jsonString != null;
                playerObject = new JSONObject(jsonString);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (intent.getBooleanExtra("loadUsers", true)){
            setContentView(R.layout.activity_admin_users);
            Button clansOptionButton = (Button)findViewById(R.id.orgsButton);
            Button newUserButton = (Button)findViewById(R.id.newUserButton);
            Button changeUsernameButton = (Button)findViewById(R.id.usernameChangeButton);
            Button changePasswordButton = (Button)findViewById(R.id.passwordChangeButton);
            Button changePriviledgesButton = (Button)findViewById(R.id.updatePriviledgesButton);
            Button changeOrgButton = (Button)findViewById(R.id.orgChangeButton);
            Button leaveOrgButton = (Button)findViewById(R.id.orgLeaveButton);
            Button deleteUserButton = (Button)findViewById(R.id.deleteUserButton);
            usernameEntry = (EditText)findViewById(R.id.usernameTextEntry);
            passwordEntry = (EditText)findViewById(R.id.passwordTextEntry);
            orgCodeEntry = (EditText)findViewById(R.id.orgChangeTextEntry);
            newUserAdmin = (CheckBox)findViewById(R.id.isAdminCheckbox);
            newUserQuestionCreator = (CheckBox)findViewById(R.id.isQuestionMasterCheckbox);
            isAdminCheck =(CheckBox)findViewById(R.id.changeIsAdminCheckbox);
            isQuestionCreatorCheck = (CheckBox)findViewById(R.id.changeIsQuestionMasterCheckbox);
            usernameChangeEntry = (EditText)findViewById(R.id.usernameChangeTextEntry);
            passwordChangeEntry = (EditText)findViewById(R.id.passwordChangeTextEntry);
            responseText = (TextView)findViewById(R.id.responseText);

            clansOptionButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * Refreshed the admin page to view clans
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdminActivity.this, AdminActivity.class);
                    intent.putExtra("playerObject", playerObject.toString());
                    intent.putExtra("loadUsers", false);
                    startActivity(intent);
                }
            });
            changeUsernameButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * Changes a user's username with the entry provided
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    try {
                        JSONObject player = objects.getJSONObject(spinner.getSelectedItemPosition());
                        player.put("playerUsername", usernameChangeEntry.getText().toString());
                        editUser(requestQueue, player);
                    }
                    catch(JSONException e){
                        responseText.setText("Could not edit user's password");
                    }
                }
            });
            changePasswordButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * Changes a user's username with the entry provided
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    try {
                        JSONObject player = objects.getJSONObject(spinner.getSelectedItemPosition());
                        player.put("playerHashedPassword", hashString(passwordChangeEntry.getText().toString()));
                        editUser(requestQueue, player);
                    }
                    catch(JSONException e){
                        responseText.setText("Could not edit user's password");
                    }
                }
            });
            changeOrgButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * Changes a user's org with the entry provided
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    JSONObject player = null;
                    try {
                        player = objects.getJSONObject(spinner.getSelectedItemPosition());
                        String newOrg = orgCodeEntry.getText().toString();
                        changeOrg(player, newOrg);
                    } catch (JSONException e) {
                        responseText.setText("Could not change organization");
                    }

                }
            });
            leaveOrgButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * Leaves the user's organization
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    JSONObject player = null;
                    try {
                        player = objects.getJSONObject(spinner.getSelectedItemPosition());
                    } catch (JSONException e) {
                        responseText.setText("Could not leave organization");
                    }
                    //default organization
                    String newOrg = "2002";
                    changeOrg(player, newOrg);

                }
            });
            deleteUserButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * Deletes the selected user
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    String url = "http://coms-309-041.class.las.iastate.edu:8080/users/";
                    JSONObject player = null;
                    int id = 0;
                    try {

                        player = objects.getJSONObject(spinner.getSelectedItemPosition());
                        id = player.getInt("playerId");
                    } catch (JSONException e) {
                        responseText.setText(e.toString());
                    }
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

                                    responseText.setText("Successfully deleted user");
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
                                    responseText.setText("Could not delete user");
                                }
                            }
                    );

                    // Add the request to the provided Volley queue
                    requestQueue.add(jsonObjectRequest);
                }
            });
            newUserButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * Creates a new user using the creditials provided
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v){

                    String username = usernameEntry.getText().toString();
                    int isAdminNum;
                    if (newUserAdmin.isChecked()){
                        isAdminNum = 1;
                    }
                    else{
                        isAdminNum = 0;
                    }

                    int isQuestionCreatorNum;
                    if (newUserQuestionCreator.isChecked()){
                        isQuestionCreatorNum = 1;
                    }
                    else{
                        isQuestionCreatorNum = 0;
                    }
                    String password = passwordEntry.getText().toString();

                    String url = "http://coms-309-041.class.las.iastate.edu:8080/users/post/" + username + '/' + hashString(password) +'/' + isAdminNum + '/' + isQuestionCreatorNum;
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
                                    responseText.setText("Success: New user added");
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
                                    responseText.setText(error.toString());
                                }
                            }
                    );

                    // Add the request to the provided Volley queue
                    requestQueue.add(jsonObjectRequest);
                }
            });
            changePriviledgesButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * Changes a user's privileges with the checkboxes provided
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    try {
                        JSONObject player = objects.getJSONObject(spinner.getSelectedItemPosition());
                        if(isAdminCheck.isChecked()){
                            player.put("admin", true);
                        }
                        else{
                            player.put("admin", false);
                        }
                        if(isQuestionCreatorCheck.isChecked()){
                            player.put("questionMaster", true);
                        }
                        else{
                            player.put("questionMaster", false);

                        }
                        editUser(requestQueue, player);
                    }
                    catch(JSONException e){
                        responseText.setText("Could not edit user");
                    }
                }
            });


        }
        else{
            setContentView(R.layout.activity_admin_clans);
            Button usersOptionButton = (Button)findViewById(R.id.usersButton);
            neworgNameEntry = (EditText)findViewById(R.id.clanTextEntry);
            orgNameEntry = (EditText)findViewById(R.id.clanChangeTextEntry);
            Button newOrgButton = (Button)findViewById(R.id.newClanButton);
            Button changeOrgButton = (Button)findViewById(R.id.clanChangeButton);
            Button deleteOrgButton = (Button)findViewById(R.id.clanDeleteButton);
            responseText = (TextView)findViewById(R.id.responseText);

            usersOptionButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * Refreshed the admin page to view users
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdminActivity.this, AdminActivity.class);
                    intent.putExtra("playerObject", playerObject.toString());
                    intent.putExtra("loadUsers", true);
                    startActivity(intent);
                }
            });
            changeOrgButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * Changes a clan's name with the entry provided
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    try {
                        JSONObject org = objects.getJSONObject(spinner.getSelectedItemPosition());
                        int orgId = 0;
                        orgId = org.getInt("id");
                        String orgName = orgNameEntry.getText().toString();
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                                Request.Method.PUT,
                                "http://coms-309-041.class.las.iastate.edu:8080/organizations/" + orgId + "/" + orgName,
                                null,
                                new Response.Listener<JSONObject>() {
                                    /**
                                     * On a successful response, display the success message
                                     * @param response The Volley returned JSONObject of the user
                                     */
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Handle the JSON response
                                        responseText.setText("Success: Clan name changed");
                                    }
                                },
                                new Response.ErrorListener() {
                                    /**
                                     * On an unsuccessful PUT request, display the error to the screen
                                     * @param error The Volley error that caused a failure in the PUT request
                                     */
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // Handle errors
                                        responseText.setText("Error: " + error.toString());
                                    }
                                }
                                );
                        // Add the request to the provided Volley queue
                        requestQueue.add(jsonObjectRequest);
                    } catch (JSONException e) {
                        responseText.setText("Error: " + e.toString());
                    }
                }
            });
            deleteOrgButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * Deletes the selected organization, resetting all clan's players to the default clan
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    try {
                        JSONObject org = objects.getJSONObject(spinner.getSelectedItemPosition());
                        int id = 0;
                        id = org.getInt("id");
                    StringRequest request = new StringRequest(
                          Request.Method.DELETE,
                            "http://coms-309-041.class.las.iastate.edu:8080/organizations/" + id + "/" + objects.getJSONObject(configurationID).getString("orgName"),
                            new Response.Listener<String>() {
                                /**
                                 * On a successful response, display the success message
                                 * @param response The Volley returned String name of the organization deleted
                                 */
                                @Override
                                public void onResponse(String response) {
                                    // Handle the String response
                                    responseText.setText("Success: Clan deleted");
                                }
                            },
                            new Response.ErrorListener() {
                                /**
                                 * On an unsuccessful DELETE request, display the error to the screen
                                 * @param error The Volley error that caused a failure in the DELETE request
                                 */
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Handle errors
                                    responseText.setText("Error deleting clan");
                                }
                            }
                            );
                        // Add the request to the provided Volley queue
                        requestQueue.add(request);
                    } catch (JSONException e) {
                        responseText.setText("Error: could not delete clan");
                    }
                }

            });
            newOrgButton.setOnClickListener(new View.OnClickListener() {
                /**
                 * Creates a new clan using the name provided
                 * @param v The view that was clicked.
                 */
                @Override
                public void onClick(View v) {
                    JsonObjectRequest request = new JsonObjectRequest(
                            Request.Method.POST,
                            "http://coms-309-041.class.las.iastate.edu:8080/organizations/post/" + neworgNameEntry.getText().toString(),
                            null,
                            new Response.Listener<JSONObject>() {
                                /**
                                 * On a successful response, display the success message
                                 * @param response The Volley returned JSON object of the organization added
                                 */
                                @Override
                                public void onResponse(JSONObject response) {
                                    // Handle the String response
                                    responseText.setText("Success: Clan added");
                                }
                            },
                            new Response.ErrorListener() {
                                /**
                                 * On an unsuccessful GET request, display the error to the screen
                                 * @param error The Volley error that caused a failure in the GET request
                                 */
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // Handle errors
                                    responseText.setText("Error adding clan");
                                }
                            }
                    );

                    // Add the request to the provided Volley queue
                    requestQueue.add(request);
                }
            });
        }
        Button backButton = (Button)findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Sends the user back to the Game Menu with its information
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, GameMenuActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                startActivity(intent);
            }
        });
        getArray(intent.getBooleanExtra("loadUsers", true));
    }

    /**
     * Edits a user with a volley request
     * @param requestQueue The Volley request queue to submit the PUT request
     * @param player the JSONObject of the player
     * @throws JSONException If the JSONObject was queried incorrectly this is thrown.
     */
    public void editUser(RequestQueue requestQueue, JSONObject player) throws JSONException {
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
                        responseText.setText("User Successfully edited");
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
                        responseText.setText("Could not edit user");
                    }
                }
        );

        // Add the request to the provided Volley queue
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Changes a user's organization through a Volley request
     * @param player the jsonObject of the player
     * @param newOrg the string of the id of the player's new organization
     */
    private void changeOrg(JSONObject player, String newOrg){
        String url = "http://coms-309-041.class.las.iastate.edu:8080/userorg/";
        int id = 0;
        try {
            id = player.getInt("playerId");
        } catch (JSONException e) {
            responseText.setText("Could not edit user's organization ");
        }
        url += id + "/" + newOrg;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    /**
                     * On a successful PUT request, the users new organization is displayed
                     * @param response The String of the user's new organization from the PUT request
                     */
                    @Override
                    public void onResponse(JSONObject response) {
                        responseText.setText("Successfully edited user's organization");
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
                        responseText.setText("Could not edit user's organization");

                    }
                }
        );

        // Add the request to the provided Volley queue
        requestQueue.add(jsonObjectRequest);
    }
    /**
     * Gets and saved an array of users/orgs and their names
     * @param getUsers True if you want to save the array of users, false if organizations
     */
    private void getArray(boolean getUsers){
        String url = "http://coms-309-041.class.las.iastate.edu:8080";
        if(getUsers){
            url += "/users";
        }
        else{
            url +="/organizations";
        }
        JsonArrayRequest arrayRequest = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    /**
                     * On a successful GET request, the array object is updated
                     * @param response The JSONArray of the objects request
                     */
                    @Override
                    public void onResponse(JSONArray response) {
                        objects = response;
                        String[] namesArray = new String[objects.length()];
                        for(int i = 0; i < objects.length();i++){
                            try {
                                JSONObject object = objects.getJSONObject(i);
                                if(getUsers) {
                                    namesArray[i] = object.getString("playerUsername");
                                }
                                else {
                                    namesArray[i] = object.getString("orgName");
                                }
                            } catch (JSONException e) {
                                responseText.setText("Could not load data");
                            }
                        }
                        setAdapter(namesArray);
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * On an unsuccessful GET request, the Volley Error is displayed to the screen
                     * @param error The Volley error causing the request to fail
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        responseText.setText(error.toString());

                    }
                }
        );
        requestQueue.add(arrayRequest);
    }

    private void setAdapter(String[] namesArray){
        names = namesArray;

        //set dropdown
        spinner = (Spinner)findViewById(R.id.configurations);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Sets the instance variable of which item is selected by the user
             * @param parent The AdapterView where the selection happened
             * @param view The view within the AdapterView that was clicked
             * @param position The position of the view in the adapter
             * @param id The row id of the item that is selected
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item
                configurationID = (int)id;
            }

            /**
             * Handles the case where nothing is selected, chooses first option
             * @param parent The AdapterView that now contains no selected item.
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected.
                configurationID = 0;
            }
        });
    }
    public int getLastIndex(){
        return names.length - 1;
    }

}
