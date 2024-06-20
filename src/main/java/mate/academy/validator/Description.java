package mate.academy.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotNull
@Size(min = 1)
@Constraint(validatedBy = {})
@Target({ ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {
    String message() default "Invalid Description";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
