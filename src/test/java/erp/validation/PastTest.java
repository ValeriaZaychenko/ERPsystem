package erp.validation;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = erp.config.JUnitConfiguration.class)
public class PastTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    static class TestPastDate {
        TestPastDate (LocalDate date)
        {
            this.date = date;
        }

        @Past(value = DateBorder.ONLY_PAST)
        LocalDate date;
    }

    @Test
    public void testPastInvalid() {
        TestPastDate date = new TestPastDate(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<TestPastDate>> constraintViolations = validator.validate(date);

        assertEquals(1, constraintViolations.size());
        assertEquals(ValidationMessages.FutureDate, constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void testPastInvalidToday() {
        TestPastDate date = new TestPastDate(LocalDate.now());
        Set<ConstraintViolation<TestPastDate>> constraintViolations = validator.validate(date);

        assertEquals(1, constraintViolations.size());
        assertEquals(ValidationMessages.FutureDate, constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void testPastCorrect() {
        TestPastDate date = new TestPastDate(LocalDate.now().minusDays(1));
        Set<ConstraintViolation<TestPastDate>> constraintViolations = validator.validate(date);

        assertEquals(0, constraintViolations.size());
    }

    static class TestPastDateIncludeToday {
        TestPastDateIncludeToday (LocalDate date)
        {
            this.date = date;
        }

        @Past(value = DateBorder.INCLUDE_TODAY)
        LocalDate date;
    }

    @Test
    public void testPastIncludeInvalid() {
        TestPastDateIncludeToday date = new TestPastDateIncludeToday(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<TestPastDateIncludeToday>> constraintViolations = validator.validate(date);

        assertEquals(1, constraintViolations.size());
        assertEquals(ValidationMessages.FutureDate, constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void testPastIncludeTodayCorrect() {
        TestPastDateIncludeToday date = new TestPastDateIncludeToday(LocalDate.now());
        Set<ConstraintViolation<TestPastDateIncludeToday>> constraintViolations = validator.validate(date);

        assertEquals(0, constraintViolations.size());
    }

    @Test
    public void testPastIncludeTodayCorrectPast() {
        TestPastDateIncludeToday date = new TestPastDateIncludeToday(LocalDate.now().minusDays(1));
        Set<ConstraintViolation<TestPastDateIncludeToday>> constraintViolations = validator.validate(date);

        assertEquals(0, constraintViolations.size());
    }
}

