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

    @Test
    public void test() {
        TestDate date = new TestDate(LocalDate.now().plusDays(1));
        Set<ConstraintViolation<TestDate>> constraintViolations = validator.validate(date);

        assertEquals(1, constraintViolations.size());
        assertEquals("date is not in the past", constraintViolations.iterator().next().getMessage());
    }

    static class TestDate {
        TestDate (LocalDate date)
        {
            this.date = date;
        }

        @Past
        LocalDate date;
    }
}

