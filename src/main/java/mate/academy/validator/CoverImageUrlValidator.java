package mate.academy.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class CoverImageUrlValidator implements ConstraintValidator<CoverImage, String> {
    private static final String IMAGE_URL_REGEX =
            "^(http(s?):)([/|.|\\w|\\s|-])*\\.(?:jpg|jpeg|png|gif)$";
    private static final Pattern IMAGE_URL_PATTERN = Pattern.compile(IMAGE_URL_REGEX,
            Pattern.CASE_INSENSITIVE);

    @Override
    public boolean isValid(String coverImage, ConstraintValidatorContext context) {
        return coverImage != null && IMAGE_URL_PATTERN
                .matcher(coverImage)
                .matches();
    }
}
