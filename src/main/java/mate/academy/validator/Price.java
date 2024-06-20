package mate.academy.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotNull
@DecimalMin(value = "0.0", inclusive = false)
@Constraint(validatedBy = {})
@Target({ ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Price {
    String message() default "Invalid Price";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
