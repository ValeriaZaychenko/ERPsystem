package erp.validation;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.Temporal;

public class PastOrTodayValidator implements ConstraintValidator<PastOrToday, Temporal> {

    @Override
    public void initialize(PastOrToday constraintAnnotation) {
    }

    @Override
    public boolean isValid(Temporal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDate ld = LocalDate.from(value);
        if (ld.isBefore(LocalDate.now()) || ld.isEqual(LocalDate.now())) {
            return true;
        }
        return false;
    }
}
