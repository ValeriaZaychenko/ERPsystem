package erp.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ PARAMETER, ANNOTATION_TYPE, METHOD, FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = PastValidator.class)
@Documented
public @interface Past {

    String message() default ValidationMessages.FutureDate;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    DateBorder value();

    @Target({ PARAMETER, ANNOTATION_TYPE, METHOD, FIELD })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Past[] value();
    }
}

