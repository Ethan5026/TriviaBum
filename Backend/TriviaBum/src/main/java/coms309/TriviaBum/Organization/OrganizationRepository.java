package coms309.TriviaBum.Organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    @Query(value = "SELECT o FROM Organization o " +
            "JOIN User u ON o.orgid = u.organization.orgid " +
            "GROUP BY o.orgid, o.orgName " +
            "ORDER BY SUM(u.totalCorrect) DESC " +
            "LIMIT 5")
    List<Organization> findTop5ByOrderByTotalCorrectDesc();

    Organization findByOrgid(int orgId);

    Organization findByOrgName(String orgName);

    boolean existsByOrgName(String orgName);

}
