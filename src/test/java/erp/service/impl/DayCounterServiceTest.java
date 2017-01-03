package erp.service.impl;

import erp.exceptions.DateOrderException;
import erp.service.IDayCounterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = erp.config.JUnitConfiguration.class)
@Transactional
public class DayCounterServiceTest {

    @Inject
    private IDayCounterService dayCounterService;

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
}
