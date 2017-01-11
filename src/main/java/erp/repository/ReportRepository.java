package erp.repository;

import erp.domain.Report;
import erp.domain.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface ReportRepository extends CrudRepository<Report, String> {

    List<Report> findByUserOrderByDateDesc(User user);

    List<Report> findAll();

    @Query("SELECT r FROM reports r where r.user = :user AND ( r.date >= :beginDate AND r.date <= :endDate )")
    List<Report> findByUserAndBetweenQuery(@Param("beginDate")LocalDate beginDate,
                                          @Param("endDate")LocalDate endDate,
                                          @Param("user")User user);

    @Query("DELETE FROM reports r WHERE r.user.id = :id")
    @Modifying
    void removeByUserIdEquals(@Param("id")String id);

    @Query("SELECT r FROM reports r WHERE r.user.id = :id AND r.date = :date")
    Stream<Report> findAllByCustomQueryNotNull(@Param("id")String id,
                                 @Param("date")LocalDate date);
}
