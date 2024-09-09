package coms309.TriviaBum.Users;

import coms309.TriviaBum.Organization.Organization;
import coms309.TriviaBum.Organization.OrganizationRepository;
import coms309.TriviaBum.Questions.PowerUp.PowerUpRepository;
import coms309.TriviaBum.Questions.PowerUp.Powerup;
import coms309.TriviaBum.Users.User;
import coms309.TriviaBum.Users.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class UserController {

    @Autowired
    PowerUpRepository powerUpRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    ProfileRepository profileRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";


    @Operation(summary = "Method to get all users")
    @GetMapping(path = "users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Operation(summary = "Method to make a new user")
    @PostMapping(path = "users/post/{username}/{password}/{isAdmin}/{isQuestionMaster}")
    User PostUserByPath(@PathVariable String username, @PathVariable String password, @PathVariable int isAdmin, @PathVariable int isQuestionMaster) {

        if(userRepository.existsByPlayerUsername(username)) {
            throw new UsernameAlreadyExistsException("Username already exists: " + username);
        }
        //create a new powerup object and save it to powerup table
        Powerup power = new Powerup();
        powerUpRepository.save(power);

        User newUser = new User(username, password);


        if (isAdmin == 1) {
            newUser.setAdmin(true);
        }
        if (isQuestionMaster == 1) {
            newUser.setQuestionMaster(true);
        }

        Optional<Organization> optionalOrganization = organizationRepository.findById(2002L);
        Organization newOrganization;
        newOrganization = optionalOrganization.orElseGet(Organization::new);

        Profile newProfile = new Profile();
        newUser.setOrganization(newOrganization);
        newUser.setProfile(newProfile);

        //set the user to have the powerup generated earlier
        newUser.setPowerup(power);


        newProfile.setUser(newUser);
        newUser = userRepository.save(newUser);
        profileRepository.save(newProfile);



        return newUser;


    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class UsernameAlreadyExistsException extends RuntimeException {
        public UsernameAlreadyExistsException(String message) {
            super(message);
        }

    }

    @Operation(summary = "Method to get user's json object by their username")
    @GetMapping(path = "users/{username}")
    User GetUserByPath(@PathVariable String username) {
        List<User> userList = userRepository.findAll();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getPlayerUsername().equals(username)) {
                return userList.get(i);
            }
        }
        return null;
    }

    @Operation(summary = "Method to update user information by ID and user json object")
    @PutMapping("users/{id}")
    Optional<User> updateUser(@PathVariable Long id, @RequestBody User request) {
        if (userRepository.findById(id) == null) {
            return null;
        }
        if (request.getTotalQuestions() != 0) {
            double totalCorrect = request.getTotalCorrect();
            double totalQuestions = request.getTotalQuestions();

            request.setCorrectRatio(Math.round((totalCorrect / totalQuestions) * 100.0) / 100.0);


        }
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.get();

        BeanUtils.copyProperties(request, user, "id", "organization","profile", "powerup");

        userRepository.save(user);
        return userRepository.findById(id);
    }

    @Operation(summary = "Method to delete a user by ID")
    @DeleteMapping(path = "users/{id}")
    String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return success;
    }

    @Operation(summary = "Method to get a user's total questions answered")
    @GetMapping(path = "users/{username}/totalquestions")
    int GetTotalQuestions(@PathVariable String username) {

        List<User> userList = userRepository.findAll();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getPlayerUsername().equals(username)) {
                return userList.get(i).getTotalQuestions();
            }
        }
        return 0;
    }

    @Operation(summary = "Method to get a user's total correct questions")
    @GetMapping(path = "users/{username}/totalcorrect")
    int GetTotalCorrect(@PathVariable String username) {
        List<User> userList = userRepository.findAll();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getPlayerUsername().equals(username)) {
                return userList.get(i).getTotalCorrect();
            }
        }
        return 0;

    }

    @Operation(summary = "Method to get a user's current streak")
    @GetMapping(path = "users/{username}/currenstreak")
    int GetCurrentStreak(@PathVariable String username) {
        List<User> userList = userRepository.findAll();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getPlayerUsername().equals(username)) {
                return userList.get(i).getCurrentStreak();
            }
        }
        return 0;

    }

    @Operation(summary = "Method to get a user's longest streak")
    @GetMapping(path = "users/{username}/longeststreak")
    int GetLongestStreak(@PathVariable String username) {
        List<User> userList = userRepository.findAll();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getPlayerUsername().equals(username)) {
                return userList.get(i).getLongestStreak();
            }
        }
        return 0;

    }

    @Operation(summary = "Method to get get a user's correct ratio")
    @GetMapping(path = "users/{username}/correctratio")
    double GetCorrectRatio(@PathVariable String username) {
        List<User> userList = userRepository.findAll();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getPlayerUsername().equals(username)) {
                return userList.get(i).getCorrectRatio();
            }
        }
        return 0;

    }

    @Operation(summary = "Method to assign user to organization")
    @PutMapping(path = "userorg/{userId}/{newOrgId}")
    public User setUserOrganization(@PathVariable Long userId, @PathVariable Long newOrgId) {
        // Retrieve the user by ID
        Optional<User> optionalUser = userRepository.findById(userId);

        User user = optionalUser.get();

            // Retrieve the organization by ID


        Organization organization = user.getOrganization();
        if(organization != null) {
            organization.removeUser(user);
        }

        Optional<Organization> optionalOrganization = organizationRepository.findById(newOrgId);
        Organization newOrganization = optionalOrganization.get();

        // Update user's organization
        user.setOrganization(newOrganization);


        // Save the updated user entity
        userRepository.save(user);


        return user;
    }

    @Operation(summary = "Method to get organization that user belongs to")
    @GetMapping(path = "userorg/{username}")
    String getOrganization(@PathVariable String username) {
        List<User> userList = userRepository.findAll();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getPlayerUsername().equals(username)) {
                return userList.get(i).getOrganization().getOrgName();
            }
        }
        return null;
    }

    @Operation(summary = "Method to get the usernames of the friends of {username}")
    @GetMapping(path = "friends/{username}")
    String getFriends(@PathVariable String username) {
        List<User> userList = userRepository.findAll();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getPlayerUsername().equals(username)) {
                return userList.get(i).getFriendlist();
            }
        }
        return "";
    }

    @Operation(summary = "Method to delete a specific friend from a user{username}")
    @DeleteMapping(path = "friends/{username}/{usernametodelete}")
    String deleteFriends(@PathVariable String username, @PathVariable String usernametodelete) {
        List<User> userList = userRepository.findAll();
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getPlayerUsername().equals(username)) {
                User user = userList.get(i);
                if (user.getFriendlist().contains(usernametodelete)){
                    user.deleteFriend(usernametodelete);
                    userRepository.save(user);
                    return success;
                }
                else{
                    return "user is not friends with user you requested to delete";
                }

            }
        }
        return failure;
    }

    //mann
    @Operation(summary = "Method to add a friend to the friends list of a user {username}")
    @PostMapping(path = "friends/{username}/{usernametoadd}")
    String addFriends(@PathVariable String username, @PathVariable String usernametoadd) {
        User temp = userRepository.findByPlayerUsername(username);
        if (temp.getPlayerUsername().equals(usernametoadd)){
            return "fail, one cannot be friends with themself";
        }
        List<User> bacon = userRepository.findAll();
        for (int i = 0; i < bacon.size(); i++){
            if (bacon.get(i).getPlayerUsername().equals(usernametoadd)){
                temp.addFriend(usernametoadd);
                userRepository.save(temp);
                return "success";
            }
        }

        return "username of friend to add not found";
    }


}
