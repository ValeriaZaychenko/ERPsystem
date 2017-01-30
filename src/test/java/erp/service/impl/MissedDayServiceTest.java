package erp.service.impl;

import erp.domain.MissedDay;
import erp.domain.MissedDayType;
import erp.domain.UserRole;
import erp.dto.MissedDayDto;
import erp.exceptions.*;
import erp.service.IDayCounterService;
import erp.service.IMissedDayService;
import erp.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = erp.config.JUnitConfiguration.class)
@Transactional
@WithMockUser(username = "ram", authorities={"AUTH_ADMIN"})
public class MissedDayServiceTest {

    @Inject
    private IMissedDayService missedDayService;
    @Inject
    private IUserService userService;
    @Inject
    private IDayCounterService dayCounterService;

    @PersistenceContext
    private EntityManager entityManager;

    //--- CREATE MISSED DAY VALIDATION TESTS ---------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void createMissedDayNullDate() {
        String userId = createSimpleUser();
        missedDayService.createMissedDay(null, MissedDayType.VACATION.toString(), userId);
    }

    @Test(expected = ConstraintViolationException.class)
    public void createMissedDayNullMissedDayType() {
        String userId = createSimpleUser();
        missedDayService.createMissedDay(LocalDate.now(), null, userId);
    }

    @Test(expected = ConstraintViolationException.class)
    public void createMissedDayNullIUserId() {
        createSimpleUser();
        missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), null);
    }

    //--- CREATE MISSED DAY LOGIC TESTS --------------------------------------------------------------------------------

    @Test
    public void createMissedDayCorrectly() {
        String userId = createSimpleUser();
        String id = missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        assertNotNull(id);

        MissedDay missedDay = findMissedDayById(id);

        assertEquals(missedDay.getId(), id);
        assertEquals(missedDay.getDate(), LocalDate.now());
        assertEquals(missedDay.getMissedDayType(), MissedDayType.VACATION);
        assertEquals(missedDay.getUser().getId(), userId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void createMissedDayInvalidUserId() {
        missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), UUID.randomUUID().toString());
    }

    @Test(expected = UnknownMissedDayTypeException.class)
    public void createMissedDayParseException() {
        String userId = createSimpleUser();
        missedDayService.createMissedDay(LocalDate.now(), "INVALID", userId);
    }

    @Test(expected = HolidayOccupiedDateException.class)
    public void createMissedDayHolidayAlreadyExistException() {
        dayCounterService.createHoliday(LocalDate.now(), "holiday");

        String userId = createSimpleUser();
        missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);
    }

    @Test(expected = TwoMissedDaysInOneDateException.class)
    public void createMissedDayMissedDayAlreadyExistException() {
        String userId = createSimpleUser();
        missedDayService.createMissedDay(LocalDate.now(), MissedDayType.DAY_OFF.toString(), userId);
        missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);
    }

    //--- EDIT MISSED DAY VALIDATION TESTS -----------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void editMissedDayNullId() {
        String userId = createSimpleUser();
        String id = missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.editMissedDay(null, LocalDate.now(), MissedDayType.VACATION.toString(), userId);
    }

    @Test(expected = ConstraintViolationException.class)
    public void editMissedDayNullDate() {
        String userId = createSimpleUser();
        String id = missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.editMissedDay(id, null, MissedDayType.VACATION.toString(), userId);
    }

    @Test(expected = ConstraintViolationException.class)
    public void editMissedDayNullMissedDayType() {
        String userId = createSimpleUser();
        String id = missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.editMissedDay(id, LocalDate.now(), null, userId);
    }

    @Test(expected = ConstraintViolationException.class)
    public void editMissedDayNullUserId() {
        String userId = createSimpleUser();
        String id = missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.editMissedDay(id, LocalDate.now(),  MissedDayType.VACATION.toString(), null);
    }

    //--- EDIT MISSED DAY LOGIC TESTS ----------------------------------------------------------------------------------

    @Test(expected = EntityNotFoundException.class)
    public void editMissedDayUserInvalidId() {
        String userId = createSimpleUser();
        missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.editMissedDay(
                UUID.randomUUID().toString(), LocalDate.now(),
                MissedDayType.VACATION.toString(), userId);
    }

    @Test(expected = UnknownMissedDayTypeException.class)
    public void editMissedDayParseMissedDayTypeException() {
        String userId = createSimpleUser();
        String id = missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.editMissedDay(id, LocalDate.now(), "INVALID", userId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void editMissedDayUserInvalidUserId() {
        String userId = createSimpleUser();
        String id = missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.editMissedDay(
                id, LocalDate.now(),
                MissedDayType.VACATION.toString(), UUID.randomUUID().toString());
    }

    @Test(expected = HolidayOccupiedDateException.class)
    public void editMissedDayHolidayAlreadyExist() {
        dayCounterService.createHoliday(LocalDate.now().minusDays(3), "holiday");
        String userId = createSimpleUser();
        String id = missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.editMissedDay(id, LocalDate.now().minusDays(3), MissedDayType.VACATION.toString(), userId);
    }

    @Test(expected = TwoMissedDaysInOneDateException.class)
    public void editMissedDayDayOffAlreadyExist() {
        String userId = createSimpleUser();
        missedDayService.createMissedDay(LocalDate.now().minusDays(3), MissedDayType.VACATION.toString(), userId);
        String id = missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.editMissedDay(id, LocalDate.now().minusDays(3), MissedDayType.VACATION.toString(), userId);
    }

    @Test
    public void editMissedDayCorrectlyNothingChanged() {
        String userId = createSimpleUser();
        String id = missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.editMissedDay(id, LocalDate.now(),  MissedDayType.VACATION.toString(), userId);

        MissedDay missedDay = findMissedDayById(id);

        assertEquals(missedDay.getId(), id);
        assertEquals(missedDay.getDate(), LocalDate.now());
        assertEquals(missedDay.getMissedDayType(), MissedDayType.VACATION);
        assertEquals(missedDay.getUser().getId(), userId);
    }

    @Test
    public void editMissedDayCorrectlyDateChanged() {
        String userId = createSimpleUser();
        String id = missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.editMissedDay(id, LocalDate.now().minusDays(3),  MissedDayType.VACATION.toString(), userId);

        MissedDay missedDay = findMissedDayById(id);

        assertEquals(missedDay.getId(), id);
        assertEquals(missedDay.getDate(), LocalDate.now().minusDays(3));
        assertEquals(missedDay.getMissedDayType(), MissedDayType.VACATION);
        assertEquals(missedDay.getUser().getId(), userId);
    }

    @Test
    public void editMissedDayCorrectlyTypeChanged() {
        String userId = createSimpleUser();
        String id = missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.editMissedDay(id, LocalDate.now(),  MissedDayType.DAY_OFF.toString(), userId);

        MissedDay missedDay = findMissedDayById(id);

        assertEquals(missedDay.getId(), id);
        assertEquals(missedDay.getDate(), LocalDate.now());
        assertEquals(missedDay.getMissedDayType(), MissedDayType.DAY_OFF);
        assertEquals(missedDay.getUser().getId(), userId);
    }

    //--- DELETE MISSED DAY VALIDATION TESTS ---------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void deleteMissedDayNullId() {
        String userId = createSimpleUser();
        missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.deleteMissedDay(null);
    }

    //--- DELETE MISSED DAY LOGIC TESTS --------------------------------------------------------------------------------

    @Test(expected = EntityNotFoundException.class)
    public void deleteMissedDayInvalidId() {
        String userId = createSimpleUser();
        missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.deleteMissedDay(UUID.randomUUID().toString());
    }

    @Test(expected = NoResultException.class)
    public void deleteMissedDayCorrectly() {
        String userId = createSimpleUser();
        String id = missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);
        missedDayService.deleteMissedDay(id);

        findMissedDayById(id);
    }

    @Test(expected = EntityNotFoundException.class)
    public void deleteMissedDayTwice() {
        String userId = createSimpleUser();
        String id = missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.deleteMissedDay(id);
        missedDayService.deleteMissedDay(id);
    }

    //--- VIEW USER MISSED DAYS BETWEEN DATES VALIDATION TESTS ---------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void viewUserMissedDaysNullBeginDate() {
        String userId = createSimpleUser();
        missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.viewUserMissedDaysBetweenDates(null, LocalDate.now(), userId);
    }

    @Test(expected = ConstraintViolationException.class)
    public void viewUserMissedDaysNullEndDate() {
        String userId = createSimpleUser();
        missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.viewUserMissedDaysBetweenDates(LocalDate.now(), null, userId);
    }

    @Test(expected = ConstraintViolationException.class)
    public void viewUserMissedDaysNullUserId() {
        String userId = createSimpleUser();
        missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.viewUserMissedDaysBetweenDates(LocalDate.now().minusDays(1), LocalDate.now(), null);
    }

    //--- VIEW USER MISSED DAYS BETWEEN DATES LOGIC TESTS --------------------------------------------------------------

    @Test(expected = DateOrderException.class)
    public void viewUserMissedDaysBeginAfterEndDate() {
        String userId = createSimpleUser();
        missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.viewUserMissedDaysBetweenDates(LocalDate.now(), LocalDate.now().minusDays(1), userId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void viewUserMissedDaysInvalidUserId() {
        String userId = createSimpleUser();
        missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.viewUserMissedDaysBetweenDates(
                LocalDate.now().minusDays(1), LocalDate.now(), UUID.randomUUID().toString());
    }

    @Test
    public void viewUserMissedDaysEmptyList() {
        String userId = createSimpleUser();
        List<MissedDayDto> dtos = missedDayService.viewUserMissedDaysBetweenDates(
                LocalDate.now().minusDays(1), LocalDate.now(), userId);

        assertEquals(dtos.size(), 0);
    }

    @Test
    public void viewUserMissedDaysCorrectly() {
        String userId = createSimpleUser();
        missedDayService.createMissedDay(LocalDate.now(), MissedDayType.DAY_OFF.toString(), userId);
        missedDayService.createMissedDay(LocalDate.now().minusDays(1), MissedDayType.DAY_OFF.toString(), userId);
        List<MissedDayDto> dtos = missedDayService.viewUserMissedDaysBetweenDates(
                LocalDate.now().minusDays(2), LocalDate.now(), userId);

        assertEquals(dtos.size(), 2);
    }

    //--- MARK DAY-OFF AS VACATION VALIDATION TESTS --------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void markDayOffAsVacationNullId() {
        String userId = createSimpleUser();
        missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.markDayOffAsVacation(null);
    }

    //--- MARK DAY-OFF AS VACATION LOGIC TESTS -------------------------------------------------------------------------

    @Test(expected = EntityNotFoundException.class)
    public void markDayOffAsVacationInvalidId() {
        String userId = createSimpleUser();
        missedDayService.createMissedDay(LocalDate.now(), MissedDayType.VACATION.toString(), userId);

        missedDayService.markDayOffAsVacation(UUID.randomUUID().toString());
    }

    @Test
    public void markDayOffAsVacationCorrectly() {
        String userId = createSimpleUser();
        String id = missedDayService.createMissedDay(LocalDate.now(), MissedDayType.DAY_OFF.toString(), userId);

        missedDayService.markDayOffAsVacation(id);
        MissedDay missedDay = findMissedDayById(id);

        assertEquals(missedDay.getMissedDayType(), MissedDayType.VACATION);
    }

    //------------------------------------------------------------------------------------------------------------------

    private String createSimpleUser() {
        return userService.createUser("Petya", "petrov@gmail.com", UserRole.USER.toString());
    }

    private MissedDay findMissedDayById(String id) {
        TypedQuery<MissedDay> query =  entityManager.createQuery(
                "SELECT m FROM missedDays m WHERE m.id = '" + id + "'",
                MissedDay.class);

        return query.getSingleResult();
    }
}
