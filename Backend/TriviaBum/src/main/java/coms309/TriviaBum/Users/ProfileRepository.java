package coms309.TriviaBum.Users;

import org.springframework.stereotype.Repository;
import coms309.TriviaBum.Organization.Leaderboard;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Profile findByUser(User user);
}
