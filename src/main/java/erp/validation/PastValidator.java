package erp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.Temporal;

public class PastValidator implements ConstraintValidator<Past, Temporal> {

    private DateBorder border;

    @Override
    public void initialize(Past constraintAnnotation) {
        this.border = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Temporal value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDate ld = LocalDate.from(value);

        if(border == DateBorder.ONLY_PAST) {
            if (ld.isBefore(LocalDate.now())) {
                return true;
            }
        }

        if(border == DateBorder.INCLUDE_TODAY) {
            if (ld.isBefore(LocalDate.now()) || ld.isEqual(LocalDate.now())) {
                return true;
            }
        }

        return false;
    }
}
