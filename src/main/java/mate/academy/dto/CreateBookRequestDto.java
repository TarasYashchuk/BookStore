package mate.academy.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;
import mate.academy.validator.CoverImage;
import mate.academy.validator.Isbn;

@Data
public class CreateBookRequestDto {
    private static final int TITLE_MAX_LENGTH = 20;
    private static final int AUTHOR_MAX_LENGTH = 30;
    private static final String PRICE_MIN_VALUE = "0.0";
    private static final int DESCRIPTION_MIN_LENGTH = 1;

    @NotBlank(message = "Title cannot be blank")
    @Size(max = TITLE_MAX_LENGTH, message = "Title length must not exceed {max} characters")
    private String title;

    @NotBlank(message = "Author cannot be blank")
    @Size(max = AUTHOR_MAX_LENGTH, message = "Author length must not exceed {max} characters")
    private String author;

    @Isbn
    private String isbn;

    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = PRICE_MIN_VALUE, inclusive = false,
            message = "Price must be greater than zero")
    private BigDecimal price;

    @NotNull(message = "Description is mandatory")
    @Size(min = DESCRIPTION_MIN_LENGTH,
            message = "Description length must be greater than {min} characters")
    private String description;

    @CoverImage
    private String coverImage;
}
