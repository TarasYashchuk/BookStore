package mate.academy.dto.book;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import mate.academy.validator.CoverImage;
import mate.academy.validator.Isbn;

@Data
public class UpdateBookRequestDto {
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 20, message = "Title length must not exceed {max} characters")
    private String title;

    @NotBlank(message = "Author cannot be blank")
    @Size(max = 30, message = "Author length must not exceed {max} characters")
    private String author;

    @Isbn
    private String isbn;

    @NotNull(message = "Price is mandatory")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "Price must be greater than zero")
    private BigDecimal price;

    @NotBlank(message = "Description is mandatory")
    @Size(min = 1,
            message = "Description length must be greater than {min} characters")
    private String description;

    @CoverImage
    private String coverImage;

    @NotEmpty(message = "Categories are mandatory")
    private List<Long> categoryIds;
}
