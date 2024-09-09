package coms309.TriviaBum.Questions.TrueFalse;

import coms309.TriviaBum.Questions.TrueFalse.TrueFalse;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TrueFalseRepository extends JpaRepository<TrueFalse, Long>{

    TrueFalse findById(int id);

    void deleteById(int id);

}
