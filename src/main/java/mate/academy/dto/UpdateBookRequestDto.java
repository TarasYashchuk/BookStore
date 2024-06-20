package mate.academy.dto;

import java.math.BigDecimal;
import lombok.Data;
import mate.academy.validator.Author;
import mate.academy.validator.CoverImage;
import mate.academy.validator.Description;
import mate.academy.validator.Isbn;
import mate.academy.validator.Price;
import mate.academy.validator.Title;

@Data
public class UpdateBookRequestDto {
    @Title
    private String title;
    @Author
    private String author;
    @Isbn
    private String isbn;
    @Price
    private BigDecimal price;
    @Description
    private String description;
    @CoverImage
    private String coverImage;
}
