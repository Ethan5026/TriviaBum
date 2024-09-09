package com.example.androidapp;//Types of widgets
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

//yo was poppin4

/**
 * loads the trivia games main menu, allowing the user to edit settings use chats edit questions and play the game.
 */
public class GameMenuActivity extends AppCompatActivity {
    /**
     * button starts previous activity.
     */
    private Button backButton;

    /**
     * button that begins the play activity.
     */
    private Button startButton;

    /**
     * loads the activity to the users account settings
     */
    private Button accountButton;

    /**
     * loads the users stats activity.
     */
    private Button statsButton;

    /**
     * loads the question creator activity.
     */
    private Button creatorButton;

    /**
     * loads the leaderboard activity.
     */
    private Button leaderboardButton;

    /**
     * loads the global chat activity.
     */
    private Button globalchatButton;

    /**
     * loads the help chat activity.
     */
    private Button helpButton;

    /**
     * loads the admin activity.
     */
    private Button adminButton;

    /**
     * object that stores player data.
     */
    private JSONObject playerObject;

    /**
     * bool to check is the user is an admin, player, or question creator.
     */
    private boolean isAdmin;
    private boolean isQuestionCreator;
    /**
     * Holds the username of the player
     */
    private String username;

    /**
     * The add button for adding friends
     */
    private Button addButton;
    /**
     * The remove button for removing a friend
     */
    private Button removeButton;

    /**
     * MediaPlayer to play main theme
     */
    private MediaPlayer mediaPlayer;
    /**
     * The clans button to show the clans page
     */
    private Button clansButton;
    /**
     * Edit text to enter the username of a friend to add
     */
    private EditText usertoAdd;
    /**
     * Dropdown containing the user's friend's usernames
     */
    private Spinner friendsDropdown;

    /**
     * String array containing the user's friend's usernames
     */
    private String[] friendsArray1;

    /**
     * Marks the location for a friend in the dropdown
     */
    private int spinner_position = 0;

    private Spinner questionTypeSpinner;


    /**
     * Holds true if the user is playing as a guest, false otherwise
     */
    private boolean isGuest;

    /**
     * Text for Guests to see
     */

    private TextView guestTitle;

    /**
     * loads the activity and view from the xml file and manifest.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemenu);

        uiDesign.applyRandomBackgroundColor(this);

        Intent intent = getIntent();

        isGuest = intent.getBooleanExtra("isGuest", false);

        if (intent.hasExtra("playerObject")) {
            String jsonString = intent.getStringExtra("playerObject");

            try {
                assert jsonString != null;
                playerObject = new JSONObject(jsonString);
                username = playerObject.getString("playerUsername");
                isAdmin = playerObject.getBoolean("admin");
                isQuestionCreator = playerObject.getBoolean("questionMaster");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        backButton = (Button) findViewById(R.id.backButton);
        startButton = (Button) findViewById(R.id.startButton);
        accountButton = (Button) findViewById(R.id.accountButton);
        statsButton = (Button) findViewById(R.id.statsButton);
        creatorButton = (Button) findViewById(R.id.creatorButton);
        leaderboardButton = (Button) findViewById(R.id.leaderboardButton);
        globalchatButton = (Button) findViewById(R.id.chatButton);
        helpButton = (Button) findViewById(R.id.helpButton);
        adminButton = (Button) findViewById(R.id.adminButton);
        addButton = (Button) findViewById(R.id.addbutton);
        removeButton = (Button) findViewById(R.id.removebutton);
        usertoAdd = (EditText) findViewById(R.id.searchEditText);
        friendsDropdown = (Spinner) findViewById(R.id.playerListView);
        clansButton = (Button) findViewById(R.id.clansButton);
        Button friendsProfileButton = (Button) findViewById(R.id.profilebutton);
        Button myProfileButton = (Button) findViewById(R.id.myProfileButton);
        guestTitle = findViewById(R.id.guestTitle);

        guestTitle.setVisibility(View.INVISIBLE);

        if (isAdmin) {
            startButton.setVisibility(View.INVISIBLE);
            startButton.setClickable(false);

            statsButton.setVisibility(View.INVISIBLE);
            statsButton.setClickable(false);

        } else if (isQuestionCreator) {

            adminButton.setVisibility(View.INVISIBLE);
            adminButton.setClickable(false);

            startButton.setVisibility(View.INVISIBLE);
            startButton.setClickable(false);

            creatorButton.setVisibility(View.VISIBLE);
            creatorButton.setClickable(true);

        } else {
            creatorButton.setVisibility(View.INVISIBLE);
            creatorButton.setClickable(false);

            adminButton.setVisibility(View.INVISIBLE);
            adminButton.setClickable(false);
        }

        if(isGuest){
            statsButton.setVisibility(View.INVISIBLE);
            statsButton.setClickable(false);

            adminButton.setVisibility(View.INVISIBLE);
            adminButton.setClickable(false);

            clansButton.setVisibility(View.INVISIBLE);
            clansButton.setClickable(false);

            creatorButton.setVisibility(View.INVISIBLE);
            creatorButton.setClickable(false);

            leaderboardButton.setVisibility(View.INVISIBLE);
            leaderboardButton.setClickable(false);

            helpButton.setVisibility(View.INVISIBLE);
            helpButton.setClickable(false);

            globalchatButton.setVisibility(View.INVISIBLE);
            globalchatButton.setClickable(false);

            friendsDropdown.setVisibility(View.INVISIBLE);
            friendsDropdown.setClickable(false);

            addButton.setVisibility(View.INVISIBLE);
            addButton.setClickable(false);

            removeButton.setVisibility(View.INVISIBLE);
            removeButton.setClickable(false);

            leaderboardButton.setVisibility(View.INVISIBLE);
            leaderboardButton.setClickable(false);

            accountButton.setVisibility(View.INVISIBLE);
            accountButton.setClickable(false);

            usertoAdd.setVisibility(View.INVISIBLE);
            usertoAdd.setClickable(false);

            findViewById(R.id.friendsTitle).setVisibility(View.INVISIBLE);
            findViewById(R.id.friendsTitle).setClickable(false);

            friendsProfileButton.setVisibility(View.INVISIBLE);
            friendsProfileButton.setClickable(false);

            myProfileButton.setVisibility(View.INVISIBLE);
            myProfileButton.setClickable(false);

            guestTitle.setVisibility(View.VISIBLE);
        }

        friendsDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * dropdown for selecting the user of a clan.
             *
             * @param parentView       The AdapterView where the selection happened
             * @param selectedItemView The view within the AdapterView that was clicked
             * @param position         The position of the view in the adapter
             * @param id               The row id of the item that is selected
             */
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinner_position = position;
            }
            /**
             * does nothing when nothing is selected from the dropdown.
             *
             * @param parentView The AdapterView that now contains no selected item.
             */
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * when clicked loads the previous activity page.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                Intent intent = new Intent(GameMenuActivity.this, LoginSignupActivity.class);
                startActivity(intent);
            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            /**
             * starts the play activity page.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                Intent intent = new Intent(GameMenuActivity.this, PlayActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                intent.putExtra("isGuest", isGuest);
                intent.putExtra("selectedQuestionType", questionTypeSpinner.getSelectedItemPosition());
                startActivity(intent);
            }
        });
        accountButton.setOnClickListener(new View.OnClickListener() {
            /**
             * loads the user's account settings activity page.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                Intent intent = new Intent(GameMenuActivity.this, AccountSettingsActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                startActivity(intent);
            }
        });
        statsButton.setOnClickListener(new View.OnClickListener() {
            /**
             * loads the user's stats activity page.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;

                Intent intent = new Intent(GameMenuActivity.this, StatsActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                intent.putExtra("statsObject", playerObject.toString());
                intent.putExtra("routeTo", 0);
                startActivity(intent);
            }
        });
        creatorButton.setOnClickListener(new View.OnClickListener() {
            /**
             * loads the question creator activity page.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;

                Intent intent = new Intent(GameMenuActivity.this, QuestionCreatorActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                startActivity(intent);
            }
        });
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            /**
             * loads the leaderboard activity page.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;

                Intent intent = new Intent(GameMenuActivity.this, LeaderboardActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                startActivity(intent);
            }
        });

        globalchatButton.setOnClickListener(new View.OnClickListener() {
            /**
             * loads the global chat activity page.
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;

                Intent intent = new Intent(GameMenuActivity.this, GlobalChatActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                startActivity(intent);
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            /**
             * loads the help chat activity page
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;

                Intent intent = new Intent(GameMenuActivity.this, HelpChatActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                startActivity(intent);
            }
        });
        adminButton.setOnClickListener(new View.OnClickListener() {
            /**
             * loads the admin activity page to edit Users
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;

                Intent intent = new Intent(GameMenuActivity.this, AdminActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                intent.putExtra("loadUsers", true);
                startActivity(intent);
            }
        });
        clansButton.setOnClickListener(new View.OnClickListener() {
            /**
             * loads the admin activity page to edit Users
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;

                Intent intent = new Intent(GameMenuActivity.this, ClansActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            /**
             * loads the admin activity page to edit Users
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                addFriend(username);
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            /**
             * loads the admin activity page to edit Users
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                removeFriend(username);
            }
        });

        friendsProfileButton.setOnClickListener(new View.OnClickListener() {
            /**
             * loads the profile of a friend or searched user
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;


                Intent intent = new Intent(GameMenuActivity.this, ProfileActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                intent.putExtra("profileUsername", usertoAdd.getText().toString());
                intent.putExtra("routeTo", 0);
                startActivity(intent);
            }
        });

        myProfileButton.setOnClickListener(new View.OnClickListener() {
            /**
             * loads the profile of the player
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;


                String playerUsername = null;
                try {
                    playerUsername = playerObject.getString("playerUsername");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Intent intent = new Intent(GameMenuActivity.this, ProfileActivity.class);
                intent.putExtra("playerObject", playerObject.toString());
                intent.putExtra("profileUsername", playerUsername);
                intent.putExtra("routeTo", 0);
                startActivity(intent);
            }
        });

        questionTypeSpinner = findViewById(R.id.genreSpinner);
        ArrayAdapter<CharSequence> questionTypeAdapter = ArrayAdapter.createFromResource(
                this, R.array.genre_array, android.R.layout.simple_spinner_item
        );
        questionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        questionTypeSpinner.setAdapter(questionTypeAdapter);

        questionTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // You might want to store the selected position in a field to use it when starting PlayActivity
               int selectedQuestionTypePosition = position;  // Ensure this variable is declared in your activity
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        if(!isGuest) {
            updateFriendsList(username);
        }

        mediaPlayer = MediaPlayer.create(GameMenuActivity.this, R.raw.main_theme);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    /**
     * Method used to remove a friend from the friends list, using the search bar
     */
    public void removeFriend(String username){
        String apiUrl = "http://coms-309-041.class.las.iastate.edu:8080/friends/" + username + "/" + usertoAdd.getText().toString();

        // Make a DELETE request using Volley to delete the question
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE, apiUrl,
                new Response.Listener<String>() {
                    /**
                     * sends a message when the friend is succesfully deleted
                     * @param response - text displayed on screen.
                     */
                    @Override
                    public void onResponse(String response) {
                        updateFriendsList(username);
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * sends error message when the friend cant be removed by name or volley fails.
                     * @param error - text displayed on screen.
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GameMenuActivity.this, "Error Removing Friend", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        );

        // Add the request to the RequestQueue
        queue.add(stringRequest);

    }

    /**
     * Method for adding a friend to your friends list using the search bar.
     */
    public void addFriend(String username){
        String apiUrl = "http://coms-309-041.class.las.iastate.edu:8080/friends/" + username + "/" + usertoAdd.getText().toString();

        // Make a POST request using Volley to add the friend
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, apiUrl,
                new Response.Listener<String>() {
                    /**
                     * sends a message when the question is succesfully deleted
                     * @param response - text displayed on screen.
                     */
                    @Override
                    public void onResponse(String response) {
                        updateFriendsList(username);
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * sends error message when the question cant be found by Id or volley fails.
                     * @param error - text displayed on screen.
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GameMenuActivity.this, "Error Adding Friend", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        );

        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }

    /**
     * Updates the user's friends list
     */
    public void updateFriendsList(String username){
        StringRequest updatedPlayer = new StringRequest(
                Request.Method.GET,
                "http://coms-309-041.class.las.iastate.edu:8080/friends/" + username,
                new Response.Listener<String>() {
                    /**
                     * Sends the updated player object
                     * @param response - the updated player object
                     */
                    @Override
                    public void onResponse(String response) {
                        try {
                            playerObject.put("friendlist", response);
                        } catch (JSONException ignored) {
                        }

                        friendsArray1 = response.split(" ");
                        adapterSetup(friendsArray1);
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * sends error message when the player cant be found by username or volley fails.
                     * @param error - text displayed on screen.
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GameMenuActivity.this, "Error finding friends", Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(updatedPlayer);
        }


    /**
     * Sets up the adapter for the friends list
     * @param friendsList the list of freinds the user has
     */
    private void adapterSetup(String[] friendsList){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(GameMenuActivity.this, android.R.layout.simple_spinner_item, friendsList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        friendsDropdown.setAdapter(adapter);
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
