package coms309.TriviaBum.Organization;

import coms309.TriviaBum.Users.User;
import coms309.TriviaBum.Users.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class LeaderboardController {
    @Autowired
    LeaderboardRepository leaderboardRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrganizationRepository organizationRepository;

    @Operation(summary = "Method to get all Leaderboards of every organization")
    @GetMapping(path = "leaderboard")
    List<User> getLeaderboard() { return userRepository.findAllByOrderByCorrectRatioDesc(); }

    @Operation(summary = "Method to get Top 5 players over all organizations")
    @GetMapping(path = "leaderboard/top")
    List<User> getTopPlayers() {
        List<User> topPlayers = userRepository.findTop5ByOrderByCorrectRatioDesc();
        List<User> filledTopPlayers = new ArrayList<>();
        User nullUser = new User();
        nullUser.setPlayerUsername("Not enough data");
        for (int i = 0; i < 5; i++) {
            if (i < topPlayers.size()) {
                filledTopPlayers.add(topPlayers.get(i));
            } else {
                filledTopPlayers.add(nullUser); // Add null for remaining positions
            }
        }
        return filledTopPlayers;
    }
    @Operation(summary = "Method to get Top 5 players per organization")
   @GetMapping(path = "leaderboard/top/org/{username}")
    List<User> getOrgTopPlayers(@PathVariable String username) {

        User user = userRepository.findByPlayerUsername(username);
        List<User> topPlayers = userRepository.findByOrganizationIdOrderByCorrectRatioDesc((long) user.getOrganization().getId());
        List<User> filledTopPlayers = new ArrayList<>();
        User nullUser = new User();
        nullUser.setPlayerUsername("Not enough data");

        for (int i = 0; i < 5; i++) {
            if (i < topPlayers.size()) {
                filledTopPlayers.add(topPlayers.get(i));
            } else {
                filledTopPlayers.add(nullUser); // Add null for remaining positions
            }
        }
        return filledTopPlayers;
    }

    @Operation(summary = "Method to get Top 5 players per organization")
    @GetMapping(path = "leaderboard/top/orgs")
    List<Organization> getOrgTopOrgs() {

        List<Organization> organizations = organizationRepository.findTop5ByOrderByTotalCorrectDesc();
        List<Organization> filledTopOrgs = new ArrayList<>();
        Organization nullOrg = new Organization();
        nullOrg.setOrgName("Not enough data");

        for (int i = 0; i < 5; i++) {
            if (i < organizations.size()) {
                filledTopOrgs.add(organizations.get(i));
            } else {
                filledTopOrgs.add(nullOrg); // Add null for remaining positions
            }
        }
        return filledTopOrgs;

    }
    @Operation(summary = "Method to get leaderboard of top 5 player's on a user's friendslist")
    @GetMapping(path = "leaderboard/top/friends/{username}")
    List<User> getTop5Friends(@PathVariable String username) {
        User user = userRepository.findByPlayerUsername(username);
        User nullUser = new User();
        nullUser.setPlayerUsername("Not enough data");
        if (user == null) {
            // Handle case where user doesn't exist
            return Collections.emptyList();
        }

        String[] userFriends = user.getFriendlist().split(" ");
        List<User> userFriendsList = new ArrayList<>();

        // Populate userFriendsList and filter out null elements
        for (String friend : userFriends) {
            User friendUser = userRepository.findByPlayerUsername(friend);
            if (friendUser != null) {
                userFriendsList.add(friendUser);
            }
        }

        // Sort the list
        userFriendsList.sort(Comparator.comparingDouble(User::getCorrectRatio).reversed());

        // Add null elements if needed
        int missingFriends = Math.max(0, 5 - userFriendsList.size());
        for (int i = 0; i < missingFriends; i++) {
            userFriendsList.add(nullUser);
        }

        return userFriendsList;
    }


}
