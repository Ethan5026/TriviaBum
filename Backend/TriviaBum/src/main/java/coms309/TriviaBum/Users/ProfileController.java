package coms309.TriviaBum.Users;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProfileController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ProfileRepository profileRepository;

    @Operation(summary = "Method to get user linked to this profile")
    @GetMapping(path = "profile/{username}")
    Profile getUser(@PathVariable String username) {

        return profileRepository.findByUser(userRepository.findByPlayerUsername(username));
    }

    @Operation(summary = "Method to all Properties of profile")
    @PutMapping(path = "profile/{username}")
    Profile setProfile(@PathVariable String username, @RequestBody Profile profile) {
        Profile newProfile = profileRepository.findByUser(userRepository.findByPlayerUsername(username));

        BeanUtils.copyProperties(profile, newProfile, "user", "profile_id");


        return profileRepository.save(newProfile);
    }

    @Operation(summary = "Method to get user profile image")
    @GetMapping(path = "profile/pfp/{username}")
    String getProfileImageUrl(@PathVariable String username) {
        Profile profile = profileRepository.findByUser(userRepository.findByPlayerUsername(username));

        return profile.getProfileImageUrl();
    }

    @Operation(summary = "Method to set profile image")
    @PutMapping(path = "profile/pfp/{username}")
    void setProfileImageUrl(@PathVariable String username, @RequestBody Profile profile) {
        Profile newProfile = profileRepository.findByUser(userRepository.findByPlayerUsername(username));

        BeanUtils.copyProperties(profile, newProfile, "profile_id", "userBio","user", "fullName");

        profileRepository.save(profile);

    }

    @Operation(summary = "Method to get user bio linked to this profile")
    @GetMapping(path = "profile/{username}/bio")
    String getUserBio(@PathVariable String username) {
        Profile profile = profileRepository.findByUser(userRepository.findByPlayerUsername(username));

        return profile.getUserBio();
    }

    @Operation(summary = "Method to set profile image")
    @PutMapping(path = "profile/{username}/bio")
    void setUserBio(@PathVariable String username, @RequestBody String userbio) {
        Profile profile = profileRepository.findByUser(userRepository.findByPlayerUsername(username));

        profile.setUserBio(userbio);
        profileRepository.save(profile);

    }

    @Operation(summary = "Method to get user bio linked to this profile")
    @GetMapping(path = "profile/{username}/fullname")
    String getUserFullName(@PathVariable String username) {
        Profile profile = profileRepository.findByUser(userRepository.findByPlayerUsername(username));

        return profile.getFullName();
    }

    @Operation(summary = "Method to set profile image")
    @PutMapping(path = "profile/{username}/fullname")
    void setUserFullName(@PathVariable String username, @RequestBody String fullName) {
        Profile profile = profileRepository.findByUser(userRepository.findByPlayerUsername(username));

        profile.setFullName(fullName);
        profileRepository.save(profile);

    }

}
