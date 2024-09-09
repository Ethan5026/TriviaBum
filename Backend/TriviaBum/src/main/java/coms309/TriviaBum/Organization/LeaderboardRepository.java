package coms309.TriviaBum.Organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import coms309.TriviaBum.Organization.*;


public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {


}