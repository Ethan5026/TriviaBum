package coms309.TriviaBum.Users;

import java.util.Random;

public class Guest {

    //guest variables
    String username;
    Boolean admin = false;
    Random rand = new Random();

    Guest(){
        int guestCode = rand.nextInt(1000);
        //insert some type of code to ensure the username generated is not currently in use by another guest, or another standard account.
        username = ("Guest-" + guestCode);
    }


}
