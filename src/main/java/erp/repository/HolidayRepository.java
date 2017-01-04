package erp.repository;

import erp.domain.Holiday;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends CrudRepository<Holiday, String> {

    List<Holiday> findByDate(LocalDate date);

    @Query("SELECT h FROM holidays h where h.date >= :beginDate AND h.date <= :endDate")
    List<Holiday> findByDateBetweenQuery(@Param("beginDate") LocalDate beginDate,
                                        @Param("endDate") LocalDate endDate);
}
