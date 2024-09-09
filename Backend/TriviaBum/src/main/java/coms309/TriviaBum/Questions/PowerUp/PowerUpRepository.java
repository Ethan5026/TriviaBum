package coms309.TriviaBum.Questions.PowerUp;

import coms309.TriviaBum.Organization.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PowerUpRepository extends JpaRepository<Powerup, Long> {

}
