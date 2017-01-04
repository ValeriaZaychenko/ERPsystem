package erp.service.impl;

import erp.domain.Holiday;
import erp.dto.HolidayDto;
import erp.exceptions.DateOrderException;
import erp.exceptions.EntityNotFoundException;
import erp.repository.HolidayRepository;
import erp.service.IDayCounterService;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class DayCounterServiceTest {

    @Inject
    private IDayCounterService dayCounterService;

    @Inject
    private HolidayRepository holidayRepository;

    //---COUNT WEEKENDS TESTS-------------------------------------------------------------------------------------------

    @Test(expected = DateOrderException.class)
    public void countWeekendsEndBeforeBeginDate() {
        LocalDate begin = LocalDate.of(2016, 12, 31);
        LocalDate end = LocalDate.of(2016, 12, 1);
        dayCounterService.countWeekendsBetweenDates(begin, end);
    }

    @Test
    public void countWeekendsCorrectly() {
        LocalDate begin = LocalDate.of(2016, 12, 1);
        LocalDate end = LocalDate.of(2016, 12, 31);
        int days = dayCounterService.countWeekendsBetweenDates(begin, end);

        assertEquals(days, 9);
    }

    @Test
    public void countWeekendsCorrectlyBeginFromSaturday() {
        LocalDate begin = LocalDate.of(2016, 12, 3);
        LocalDate end = LocalDate.of(2016, 12, 31);
        int days = dayCounterService.countWeekendsBetweenDates(begin, end);

        assertEquals(days, 9);
    }

    @Test
    public void countWeekendsCorrectlyBeginFromSunday() {
        LocalDate begin = LocalDate.of(2016, 12, 4);
        LocalDate end = LocalDate.of(2016, 12, 31);
        int days = dayCounterService.countWeekendsBetweenDates(begin, end);

        assertEquals(days, 8);
    }

    @Test
    public void countWeekendsCorrectlyEndsBySaturday() {
        LocalDate begin = LocalDate.of(2016, 12, 1);
        LocalDate end = LocalDate.of(2016, 12, 10);
        int days = dayCounterService.countWeekendsBetweenDates(begin, end);

        assertEquals(days, 3);
    }

    @Test
    public void countWeekendsCorrectlyEndsBySunday() {
        LocalDate begin = LocalDate.of(2016, 12, 1);
        LocalDate end = LocalDate.of(2016, 12, 11);
        int days = dayCounterService.countWeekendsBetweenDates(begin, end);

        assertEquals(days, 4);
    }

    @Test
    public void countWeekendsCorrectlyBeginsFromSaturdayEndsBySunday() {
        LocalDate begin = LocalDate.of(2016, 12, 3);
        LocalDate end = LocalDate.of(2016, 12, 11);
        int days = dayCounterService.countWeekendsBetweenDates(begin, end);

        assertEquals(days, 4);
    }

    //---CALCULATE ALL DAYS TESTS---------------------------------------------------------------------------------------

    @Test(expected = DateOrderException.class)
    public void calculateAllDaysEndBeforeBeginDate() {
        LocalDate begin = LocalDate.of(2016, 12, 31);
        LocalDate end = LocalDate.of(2016, 12, 1);
        dayCounterService.getAllDaysQuantityBetweenDates(begin, end);
    }

    @Test
    public void calculateAllDaysCorrectly() {
        LocalDate begin = LocalDate.of(2016, 12, 1);
        LocalDate end = LocalDate.of(2016, 12, 31);
        int days = dayCounterService.getAllDaysQuantityBetweenDates(begin, end);

        assertEquals(days, 31);
    }

    @Test
    public void calculateAllDaysCorrectlyFebruary() {
        LocalDate begin = LocalDate.of(2017, 02, 1);
        LocalDate end = LocalDate.of(2017, 03, 1);
        int days = dayCounterService.getAllDaysQuantityBetweenDates(begin, end);

        assertEquals(days, 29);
    }

    //---CREATE HOLIDAY VALIDATION TESTS--------------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void createHolidayNullDate() {
        dayCounterService.createHoliday(null, "ddd");
    }

    @Test(expected = ConstraintViolationException.class)
    public void createHolidayBlankDescription() {
        dayCounterService.createHoliday(LocalDate.now(), "");
    }

    //---CREATE HOLIDAY LOGIC TESTS-------------------------------------------------------------------------------------

    @Test
    public void createHolidayCorrectly() {
        String id = dayCounterService.createHoliday(LocalDate.now(), "Company's anniversary");
        Holiday holiday = holidayRepository.findOne(id);

        assertNotNull(holiday);
        assertEquals(holiday.getDate(), LocalDate.now());
        assertEquals(holiday.getDescription(), "Company's anniversary");
    }

    //---EDIT HOLIDAY VALIDATION TESTS----------------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void editHolidayNullId() {
        dayCounterService.editHoliday(null, LocalDate.now(), "ddd");
    }

    @Test(expected = ConstraintViolationException.class)
    public void editHolidayNullDate() {
        String id = dayCounterService.createHoliday(LocalDate.now(), "aaa");
        dayCounterService.editHoliday(id, null, "ddd");
    }

    @Test(expected = ConstraintViolationException.class)
    public void editHolidayBlankDescription() {
        String id = dayCounterService.createHoliday(LocalDate.now(), "aaa");
        dayCounterService.editHoliday(id, LocalDate.now(), "");
    }

    //---EDIT HOLIDAY LOGIC TESTS---------------------------------------------------------------------------------------

    @Test(expected = EntityNotFoundException.class)
    public void editHolidayNoEntity() {
        String id = dayCounterService.createHoliday(LocalDate.now(), "Company's anniversary");
        dayCounterService.editHoliday(UUID.randomUUID().toString(), LocalDate.now(), "Company's anniversary");
    }

    @Test
    public void editHolidayDateNoModifications() {
        String id = dayCounterService.createHoliday(LocalDate.now(), "Company's anniversary");
        dayCounterService.editHoliday(id, LocalDate.now(), "Company's anniversary");
        Holiday holiday = holidayRepository.findOne(id);

        assertNotNull(holiday);
        assertEquals(holiday.getDate(), LocalDate.now());
        assertEquals(holiday.getDescription(), "Company's anniversary");
    }

    @Test
    public void editHolidayDateCorrectly() {
        String id = dayCounterService.createHoliday(LocalDate.now(), "Company's anniversary");
        dayCounterService.editHoliday(id, LocalDate.now().plusDays(1), "Company's anniversary");
        Holiday holiday = holidayRepository.findOne(id);

        assertNotNull(holiday);
        assertEquals(holiday.getDate(), LocalDate.now().plusDays(1));
        assertEquals(holiday.getDescription(), "Company's anniversary");
    }

    @Test
    public void editHolidayDescriptionCorrectly() {
        String id = dayCounterService.createHoliday(LocalDate.now(), "Company's anniversary");
        dayCounterService.editHoliday(id, LocalDate.now(), "8 march");
        Holiday holiday = holidayRepository.findOne(id);

        assertNotNull(holiday);
        assertEquals(holiday.getDate(), LocalDate.now());
        assertEquals(holiday.getDescription(), "8 march");
    }

    //---DELETE HOLIDAY VALIDATION TESTS--------------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void deleteHolidayNullId() {
        dayCounterService.deleteHoliday(null);
    }

    //---DELETE HOLIDAY LOGIC TESTS-------------------------------------------------------------------------------------

    @Test(expected = EntityNotFoundException.class)
    public void deleteHolidayNotFound() {
        dayCounterService.deleteHoliday(UUID.randomUUID().toString());
    }

    @Test
    public void deleteHolidayCorrectly() {
        String id = dayCounterService.createHoliday(LocalDate.now(), "Company's anniversary");

        assertNotNull(id);

        dayCounterService.deleteHoliday(id);

        assertNull(holidayRepository.findOne(id));
    }

    //---COPY HOLIDAY VALIDATION TESTS----------------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void copyHolidayNullId() {
        dayCounterService.copyHolidayToNextYear(null);
    }

    //---COPY HOLIDAY LOGIC TESTS---------------------------------------------------------------------------------------

    @Test(expected = EntityNotFoundException.class)
    public void copyHolidayNotFound() {
        dayCounterService.copyHolidayToNextYear(UUID.randomUUID().toString());
    }

    @Test
    public void copyHolidayCorrectly() {
        String id = dayCounterService.createHoliday(LocalDate.of(2016, 12, 31), "Company's anniversary");
        String newId = dayCounterService.copyHolidayToNextYear(id);

        Holiday holiday = holidayRepository.findOne(newId);

        assertNotEquals(id, newId);
        assertEquals(holiday.getDate(), LocalDate.of(2017, 12, 31));
    }

    @Test
    public void copyHolidayFromLeapYear() {
        String id = dayCounterService.createHoliday(LocalDate.of(2016, 2, 29), "Company's anniversary");
        String newId = dayCounterService.copyHolidayToNextYear(id);

        Holiday holiday = holidayRepository.findOne(newId);

        assertEquals(holiday.getDate(), LocalDate.of(2017, 3, 1));
    }

    //---FIND YEAR HOLIDAY VALIDATION TESTS-----------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void findYearHolidayMinYear() {
        dayCounterService.findHolidaysOneYear(-1);
    }

    @Test
    public void findYearHolidayMinYearBoundaryValue() {
        dayCounterService.findHolidaysOneYear(0);
    }

    @Test(expected = ConstraintViolationException.class)
    public void findYearHolidayMaxYear() {
        dayCounterService.findHolidaysOneYear(3001);
    }

    @Test
    public void findYearHolidayMaxYearBoundaryValue() {
        dayCounterService.findHolidaysOneYear(3000);
    }

    //---FIND YEAR HOLIDAY LOGIC TESTS----------------------------------------------------------------------------------

    @Test
    public void findHolidaysDontExist() {
        List<HolidayDto> dtos = dayCounterService.findHolidaysOneYear(2016);

        assertEquals(dtos.size(), 0);
    }

    @Test
    public void findHolidaysCorrectly() {
        dayCounterService.createHoliday(LocalDate.of(2016, 2, 29), "Company's anniversary");
        dayCounterService.createHoliday(LocalDate.of(2017, 2, 20), "Company's anniversary");
        List<HolidayDto> dtos = dayCounterService.findHolidaysOneYear(2016);

        assertEquals(dtos.size(), 1);
    }

    //---COPY YEAR HOLIDAY VALIDATION TESTS-----------------------------------------------------------------------------

    @Test(expected = ConstraintViolationException.class)
    public void copyYearHolidayMinYear() {
        dayCounterService.copyYearHolidaysToNext(-1);
    }

    @Test
    public void copyYearHolidayMinYearBoundaryValue() {
        dayCounterService.copyYearHolidaysToNext(0);
    }

    @Test(expected = ConstraintViolationException.class)
    public void copyYearHolidayMaxYear() {
        dayCounterService.copyYearHolidaysToNext(3001);
    }

    @Test
    public void copyYearHolidayMaxYearBoundaryValue() {
        dayCounterService.copyYearHolidaysToNext(3000);
    }

    //---COPY YEAR HOLIDAY LOGIC TESTS----------------------------------------------------------------------------------

    @Test
    public void copyYearHolidayCorrectly() {
        dayCounterService.createHoliday(LocalDate.of(2016, 2, 29), "Company's anniversary");
        dayCounterService.createHoliday(LocalDate.of(2016, 2, 20), "Birthday");
        dayCounterService.copyYearHolidaysToNext(2016);

        List<HolidayDto> dtos = dayCounterService.findHolidaysOneYear(2017);

        assertEquals(dtos.size(), 2);
        assertNotNull(holidayRepository.findByDate(LocalDate.of(2017, 3, 1)));
        assertNotNull(holidayRepository.findByDate(LocalDate.of(2017, 2, 20)));
    }
}
