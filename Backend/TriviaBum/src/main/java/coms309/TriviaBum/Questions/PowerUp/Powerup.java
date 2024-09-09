package coms309.TriviaBum.Questions.PowerUp;

import coms309.TriviaBum.Users.User;
import jakarta.persistence.*;

import java.util.Random;

@Entity
public class Powerup {

    @OneToOne(mappedBy = "powerup",fetch = FetchType.LAZY)
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int powerup_id;

    private int timeFreezeCount;
    private int doublePointsCount;
    private int trollCount;

    // Constructor
    public Powerup() {
        this.timeFreezeCount = 0;
        this.doublePointsCount = 0;
        this.trollCount = 0;
    }

    public int getTimeFreezeCount() {
        return timeFreezeCount;
    }

    public void setTimeFreezeCount(int timeFreezeCount) {

        this.timeFreezeCount = timeFreezeCount;
    }

    public int getDoublePointsCount() {
        return doublePointsCount;
    }

    public void setDoublePointsCount(int doublePointsCount) {
        this.doublePointsCount = doublePointsCount;
    }

    public int getTrollCount() {
        return this.trollCount;
    }
    public void setTrollCount(int trollCount){
        this.trollCount = trollCount;
    }


}