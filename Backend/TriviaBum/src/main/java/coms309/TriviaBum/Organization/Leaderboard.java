package coms309.TriviaBum.Organization;

import coms309.TriviaBum.Organization.LeaderboardRepository;
import coms309.TriviaBum.Users.UserRepository;
import coms309.TriviaBum.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "leaderboard")

public class Leaderboard {

    @OneToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @OneToMany
    private List<User> users;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long leaderboard_id;

    public Leaderboard() {

    }

    public void setLeaderboard_id(Long leaderboardId) {
        this.leaderboard_id = leaderboardId;
    }

    public Long getLeaderboard_id() {
        return leaderboard_id;
    }



}
