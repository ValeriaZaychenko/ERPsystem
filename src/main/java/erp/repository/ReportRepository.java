package erp.repository;

import erp.domain.Report;
import erp.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportRepository extends CrudRepository<Report, String> {

    List<Report> findByUserOrderByDateDesc(User user);

    List<Report> findAll();

    @Query("SELECT r FROM reports r where r.user = :user AND ( r.date >= :beginDate AND r.date <= :endDate )")
    List<Report> findByUserAndBetweenQuery(@Param("beginDate")LocalDate beginDate,
                                          @Param("endDate")LocalDate endDate,
                                          @Param("user")User user);

}
