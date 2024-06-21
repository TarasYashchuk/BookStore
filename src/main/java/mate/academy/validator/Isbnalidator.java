package mate.academy.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class Isbnalidator implements ConstraintValidator<Isbn, String> {
    private static final Pattern ISBN_10_PATTERN = Pattern
            .compile("^(?:ISBN(?:-10)?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})"
                    + "[- 0-9X]{13}$)[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");
    private static final Pattern ISBN_13_PATTERN = Pattern
            .compile("^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})"
                    + "[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$");

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        return ISBN_10_PATTERN.matcher(isbn).matches() || ISBN_13_PATTERN.matcher(isbn).matches();
    }
}
