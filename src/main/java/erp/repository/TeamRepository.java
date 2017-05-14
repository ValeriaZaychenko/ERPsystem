package erp.repository;

import erp.domain.Team;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends CrudRepository<Team, String> {

    List<Team> findAll();
    Team findByName(String nae);
}
