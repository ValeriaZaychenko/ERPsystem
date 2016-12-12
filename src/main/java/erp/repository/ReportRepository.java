package erp.repository;

import erp.domain.Report;
import erp.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends CrudRepository<Report, String> {

    List<Report> findByUser(User user);
    List<Report> findAll();
}
