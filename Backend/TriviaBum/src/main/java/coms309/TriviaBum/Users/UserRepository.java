package coms309.TriviaBum.Users;

import coms309.TriviaBum.Organization.Leaderboard;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * @author Vivek Bengre
 * 
 */ 

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findTop5ByOrderByCorrectRatioDesc();
    List<User> findAllByOrderByCorrectRatioDesc();
    List<User>findByOrganizationIdOrderByCorrectRatioDesc(Long organization_id);
    User findByPlayerUsername(String username);

    List<User> findAllByPlayerUsername(String playerUsername);

    boolean existsByPlayerUsername(String playerUsername);
}
