package coms309.TriviaBum.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import coms309.TriviaBum.Users.User;
import jakarta.persistence.*;


@Entity
public class Profile {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_player_id")
    @JsonIgnore
    private User user;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long profile_id;

    private String profileImageUrl;
    private String userBio;
    private String fullName;

    public Profile() {
        profileImageUrl = "https://static.vecteezy.com/system/resources/thumbnails/007/234/162/small/bengal-cat-resting-on-the-couch-funny-with-glasses-photo.jpg";
        userBio = "Update your bio to let your friends know what you're up to!";
        fullName = "Firstname Lastname";
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setProfile_id(Long profileId) {
        this.profile_id = profileId;
    }

    public Long getProfile_id() {
        return profile_id;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
