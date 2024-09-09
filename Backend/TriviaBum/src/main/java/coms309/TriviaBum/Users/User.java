package coms309.TriviaBum.Users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import coms309.TriviaBum.Organization.Organization;
import coms309.TriviaBum.Organization.OrganizationController;
import coms309.TriviaBum.Organization.OrganizationRepository;
import coms309.TriviaBum.Questions.PowerUp.Powerup;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 *
 * @author Eric Vo, Ethan Gruening
 * 
 */ 

@Entity
//@Table(name = "user")

public class User {

     /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */

    @OneToOne(fetch = FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = "powerup_id")
    private Powerup powerup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_orgid")
    @JsonIgnore
    private Organization organization;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @Cascade(CascadeType.ALL)
    @JsonIgnore
    private Profile profile;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int playerId;
    private String playerUsername;
    private String playerHashedPassword;

    private boolean isAdmin;
    private boolean isQuestionMaster;

    private int totalQuestions;

    private int totalCorrect;

    private int currentStreak;
    private int longestStreak;
    private double correctRatio;

    private String friendlist;


    public User(String username, String password) {

        this.playerUsername = username;
        this.playerHashedPassword = password;
        this.totalCorrect = 0;
        this.totalQuestions = 0;
        this.currentStreak = 0;
        this.longestStreak = 0;
        this.correctRatio = 0;
        this.isAdmin = false;
        this.isQuestionMaster = false;
        this.friendlist = "";
    }

    public User() {
        this.playerUsername = "username";
        this.playerHashedPassword = "password";
        this.totalCorrect = 0;
        this.totalQuestions = 0;
        this.currentStreak = 0;
        this.longestStreak = 0;
        this.correctRatio = 0;
        this.isAdmin = false;
        this.isQuestionMaster = false;
        this.friendlist = "";
    }

    // =============================== Getters and Setters for each field ================================== //

    public int getPlayerId(){
        return playerId;
    }

    public void setPlayerId(int id){
        this.playerId = id;
    }

    public String getPlayerUsername(){
        return this.playerUsername;
    }
    public void setPlayerUsername(String name){
        this.playerUsername = name;
    }

    public String getPlayerHashedPassword(){
        return this.playerHashedPassword;
    }
    public void setPlayerHashedPassword(String password){this.playerHashedPassword = password;}

    public int getTotalQuestions(){return this.totalQuestions;}
    public void setTotalQuestions(int total){this.totalQuestions=total;}

    public int getCurrentStreak(){return this.currentStreak;}
    public void setCurrentStreak(int streak){this.currentStreak=streak;}

    public int getLongestStreak(){return this.longestStreak;}
    public void setLongestStreak(int streak){this.longestStreak=streak;}

    public int getTotalCorrect(){return this.totalCorrect;}
    public void setTotalCorrect(int total){this.totalCorrect=total;}

    public double getCorrectRatio(){return this.correctRatio;}
    public void setCorrectRatio(double ratio){this.correctRatio=ratio;}

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
        organization.addUser(this);

    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        this.isAdmin = admin;
    }


    public boolean isQuestionMaster() {
        return isQuestionMaster;
    }

    public void setQuestionMaster(boolean questionMaster) {
        isQuestionMaster = questionMaster;
    }

    public String getFriendlist() {
        return friendlist;
    }


    public void deleteFriend(String usernameToDelete){
        friendlist = friendlist.replace(usernameToDelete + " ", "");
    }

    public void addFriend(String newFriend){
        friendlist += newFriend + " ";
    }

    public Powerup getPowerup() {
        return powerup;
    }

    public void setPowerup(Powerup powerup) {
        this.powerup = powerup;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
