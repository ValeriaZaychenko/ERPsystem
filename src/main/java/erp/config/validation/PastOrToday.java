package erp.config.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PastOrTodayValidator.class)
@Documented
public @interface PastOrToday {

    String message() default "date is not in the past or today";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
