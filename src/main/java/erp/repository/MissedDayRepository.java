package erp.repository;

import erp.domain.MissedDay;
import erp.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MissedDayRepository extends CrudRepository<MissedDay, String> {

    MissedDay findByUserAndDate(User user, LocalDate date);

    @Query("SELECT d FROM missedDays d where d.user = :user AND d.date >= :begin AND d.date <= :end")
    List<MissedDay> findByUserAndBetweenQuery(@Param("begin") LocalDate begin, @Param("end") LocalDate end,  @Param("user") User user);
}
