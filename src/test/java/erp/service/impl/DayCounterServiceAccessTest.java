package erp.service.impl;

import erp.domain.Holiday;
import erp.repository.HolidayRepository;
import erp.service.IDayCounterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = erp.config.JUnitConfiguration.class)
@Transactional
@WithMockUser(username = "new", authorities={"AUTH_USER"})
public class DayCounterServiceAccessTest {

    @Inject
    private IDayCounterService dayCounterService;

    @PersistenceContext
    private EntityManager entityManager;


    @Test(expected = AccessDeniedException.class)
    public void createHoliday() {
        dayCounterService.createHoliday(LocalDate.now(), "now");
    }

    @Test(expected = AccessDeniedException.class)
    public void editHoliday() {
        String id = create();

        dayCounterService.editHoliday(id, LocalDate.now().minusDays(2), "now");
    }

    @Test(expected = AccessDeniedException.class)
    public void deleteHoliday() {
        String id = create();

        dayCounterService.deleteHoliday(id);
    }

    @Test(expected = AccessDeniedException.class)
    public void copyHolidayToNextYear() {
        String id = create();

        dayCounterService.copyHolidayToNextYear(id);
    }

    @Test(expected = AccessDeniedException.class)
    public void copyAllHolidaysToNextYear() {
        String id = create();

        dayCounterService.copyYearHolidaysToNext(LocalDate.now().getYear());
    }

    private String create() {
        Holiday holiday = new Holiday(LocalDate.now(), "now");
        entityManager.persist(holiday);

        return holiday.getId();
    }
}
