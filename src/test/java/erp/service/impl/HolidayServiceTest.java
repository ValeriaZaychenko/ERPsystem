package erp.service.impl;

import erp.domain.Holiday;
import erp.dto.HolidayDto;
import erp.exceptions.DateNotUniqueException;
import erp.exceptions.EntityNotFoundException;
import erp.repository.HolidayRepository;
import erp.service.ICalendarService;
import erp.service.IHolidayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = erp.config.JUnitConfiguration.class)
@Transactional
@WithMockUser(username = "ram", authorities={"AUTH_ADMIN"})
public class HolidayServiceTest {

    @Inject
    private IHolidayService holidayService;
    @Inject
    private ICalendarService calendarService;
    @Inject
    private HolidayRepository holidayRepository;

    //---CREATE HOLIDAY VALIDATION TESTS--------------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void createHolidayNullDate() {
        holidayService.createHoliday(null, "ddd");
    }

    @Test(expected = ConstraintViolationException.class)
    public void createHolidayBlankDescription() {
        holidayService.createHoliday(LocalDate.now(), "");
    }

    //---CREATE HOLIDAY LOGIC TESTS-------------------------------------------------------------------------------------

    @Test
    public void createHolidayCorrectly() {
        String id = holidayService.createHoliday(LocalDate.now(), "Company's anniversary");
        Holiday holiday = holidayRepository.findOne(id);

        assertNotNull(holiday);
        assertEquals(holiday.getDate(), LocalDate.now());
        assertEquals(holiday.getDescription(), "Company's anniversary");
    }

    @Test(expected = DateNotUniqueException.class)
    public void createHolidayDateNotUnique() {
        holidayService.createHoliday(LocalDate.now(), "bla bla");
        holidayService.createHoliday(LocalDate.now(), "Company's anniversary");
    }

    //---EDIT HOLIDAY VALIDATION TESTS----------------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void editHolidayNullId() {
        holidayService.editHoliday(null, LocalDate.now(), "ddd");
    }

    @Test(expected = ConstraintViolationException.class)
    public void editHolidayNullDate() {
        String id = holidayService.createHoliday(LocalDate.now(), "aaa");
        holidayService.editHoliday(id, null, "ddd");
    }

    @Test(expected = ConstraintViolationException.class)
    public void editHolidayBlankDescription() {
        String id = holidayService.createHoliday(LocalDate.now(), "aaa");
        holidayService.editHoliday(id, LocalDate.now(), "");
    }

    //---EDIT HOLIDAY LOGIC TESTS---------------------------------------------------------------------------------------

    @Test(expected = EntityNotFoundException.class)
    public void editHolidayNoEntity() {
        String id = holidayService.createHoliday(LocalDate.now(), "Company's anniversary");
        holidayService.editHoliday(UUID.randomUUID().toString(), LocalDate.now(), "Company's anniversary");
    }

    @Test
    public void editHolidayDateNoModifications() {
        String id = holidayService.createHoliday(LocalDate.now(), "Company's anniversary");
        holidayService.editHoliday(id, LocalDate.now(), "Company's anniversary");
        Holiday holiday = holidayRepository.findOne(id);

        assertNotNull(holiday);
        assertEquals(holiday.getDate(), LocalDate.now());
        assertEquals(holiday.getDescription(), "Company's anniversary");
    }

    @Test
    public void editHolidayDateCorrectly() {
        String id = holidayService.createHoliday(LocalDate.now(), "Company's anniversary");
        holidayService.editHoliday(id, LocalDate.now().plusDays(1), "Company's anniversary");
        Holiday holiday = holidayRepository.findOne(id);

        assertNotNull(holiday);
        assertEquals(holiday.getDate(), LocalDate.now().plusDays(1));
        assertEquals(holiday.getDescription(), "Company's anniversary");
    }

    @Test(expected = DateNotUniqueException.class)
    public void editHolidayDateNotUnique() {
        holidayService.createHoliday(LocalDate.now().minusDays(1), "bla bla");
        String id = holidayService.createHoliday(LocalDate.now(), "Company's anniversary");
        holidayService.editHoliday(id, LocalDate.now().minusDays(1), "Company's anniversary");
    }

    @Test
    public void editHolidayDescriptionCorrectly() {
        String id = holidayService.createHoliday(LocalDate.now(), "Company's anniversary");
        holidayService.editHoliday(id, LocalDate.now(), "8 march");
        Holiday holiday = holidayRepository.findOne(id);

        assertNotNull(holiday);
        assertEquals(holiday.getDate(), LocalDate.now());
        assertEquals(holiday.getDescription(), "8 march");
    }

    //---DELETE HOLIDAY VALIDATION TESTS--------------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void deleteHolidayNullId() {
        holidayService.deleteHoliday(null);
    }

    //---DELETE HOLIDAY LOGIC TESTS-------------------------------------------------------------------------------------

    @Test(expected = EntityNotFoundException.class)
    public void deleteHolidayNotFound() {
        holidayService.deleteHoliday(UUID.randomUUID().toString());
    }

    @Test
    public void deleteHolidayCorrectly() {
        String id = holidayService.createHoliday(LocalDate.now(), "Company's anniversary");

        assertNotNull(id);

        holidayService.deleteHoliday(id);

        assertNull(holidayRepository.findOne(id));
    }

    //---COPY HOLIDAY VALIDATION TESTS----------------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void copyHolidayNullId() {
        holidayService.copyHolidayToNextYear(null);
    }

    //---COPY HOLIDAY LOGIC TESTS---------------------------------------------------------------------------------------

    @Test(expected = EntityNotFoundException.class)
    public void copyHolidayNotFound() {
        holidayService.copyHolidayToNextYear(UUID.randomUUID().toString());
    }

    @Test
    public void copyHolidayCorrectly() {
        String id = holidayService.createHoliday(LocalDate.of(2016, 12, 31), "Company's anniversary");
        String newId = holidayService.copyHolidayToNextYear(id);

        Holiday holiday = holidayRepository.findOne(newId);

        assertNotEquals(id, newId);
        assertEquals(holiday.getDate(), LocalDate.of(2017, 12, 31));
    }

    @Test
    public void copyHolidayFromLeapYear() {
        String id = holidayService.createHoliday(LocalDate.of(2016, 2, 29), "Company's anniversary");
        String newId = holidayService.copyHolidayToNextYear(id);

        Holiday holiday = holidayRepository.findOne(newId);

        assertEquals(holiday.getDate(), LocalDate.of(2017, 3, 1));
    }

    @Test(expected = DateNotUniqueException.class)
    public void copyHolidayToNotUniqueDate() {
        String id0 = holidayService.createHoliday(LocalDate.of(2016, 2, 20), "Company's anniversary");
        holidayService.createHoliday(LocalDate.of(2017, 2, 20), "Company's anniversary");
        holidayService.copyHolidayToNextYear(id0);
    }

    //---FIND YEAR HOLIDAY VALIDATION TESTS-----------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void findYearHolidayMinYear() {
        holidayService.findHolidaysOfYear(-1);
    }

    @Test
    public void findYearHolidayMinYearBoundaryValue() {
        holidayService.findHolidaysOfYear(0);
    }

    @Test(expected = ConstraintViolationException.class)
    public void findYearHolidayMaxYear() {
        holidayService.findHolidaysOfYear(3001);
    }

    @Test
    public void findYearHolidayMaxYearBoundaryValue() {
        holidayService.findHolidaysOfYear(3000);
    }

    //---FIND YEAR HOLIDAY LOGIC TESTS----------------------------------------------------------------------------------

    @Test
    public void findHolidaysDontExist() {
        List<HolidayDto> dtos = holidayService.findHolidaysOfYear(2016);

        assertEquals(dtos.size(), 0);
    }

    @Test
    public void findHolidaysCorrectly() {
        holidayService.createHoliday(LocalDate.of(2016, 2, 29), "Company's anniversary");
        holidayService.createHoliday(LocalDate.of(2017, 2, 20), "Company's anniversary");
        List<HolidayDto> dtos = holidayService.findHolidaysOfYear(2016);

        assertEquals(dtos.size(), 1);
    }

    //---FIND HOLIDAY OF YEAR SORTED TESTS------------------------------------------------------------------------------

    @Test
    public void findHolidaysSortedCorrectly() {
        String idSecond = holidayService.createHoliday(LocalDate.of(2016, 2, 29), "Company's anniversary");
        String idLast = holidayService.createHoliday(LocalDate.of(2016, 12, 20), "Company's anniversary");
        String idFirst = holidayService.createHoliday(LocalDate.of(2016, 1, 10), "Company's anniversary");
        List<HolidayDto> dtos = holidayService.findHolidaysOfYear(2016);

        assertEquals(dtos.get(0).getId(), idLast);
        assertEquals(dtos.get(1).getId(), idSecond);
        assertEquals(dtos.get(2).getId(), idFirst);
    }

    //---COPY YEAR HOLIDAY VALIDATION TESTS-----------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void copyYearHolidayMinYear() {
        holidayService.copyYearHolidaysToNext(-1);
    }

    @Test
    public void copyYearHolidayMinYearBoundaryValue() {
        holidayService.copyYearHolidaysToNext(0);
    }

    @Test(expected = ConstraintViolationException.class)
    public void copyYearHolidayMaxYear() {
        holidayService.copyYearHolidaysToNext(3001);
    }

    @Test
    public void copyYearHolidayMaxYearBoundaryValue() {
        holidayService.copyYearHolidaysToNext(3000);
    }

    //---COPY YEAR HOLIDAY LOGIC TESTS----------------------------------------------------------------------------------

    @Test
    public void copyYearHolidayCorrectly() {
        holidayService.createHoliday(LocalDate.of(2016, 2, 29), "Company's anniversary");
        holidayService.createHoliday(LocalDate.of(2016, 2, 20), "Birthday");
        holidayService.createHoliday(LocalDate.of(2017, 2, 20), "Birthday");
        holidayService.copyYearHolidaysToNext(2016);

        List<HolidayDto> dtos = holidayService.findHolidaysOfYear(2017);

        assertEquals(dtos.size(), 2);
        assertNotNull(holidayRepository.findByDate(LocalDate.of(2017, 3, 1)));
        assertNotNull(holidayRepository.findByDate(LocalDate.of(2017, 2, 20)));
    }
}
