package mate.academy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Schema(description = "Data transfer object for Book")
public class BookDto {
    @Schema(name = "ID of the book", example = "1", required = true)
    private Long id;
    @Schema(name = "Title of the book", example = "Harry Potter", required = true)
    private String title;

    @Schema(name = "Author of the book", example = "J.K. Rowling", required = true)
    private String author;

    @Schema(name = "ISBN of the book", example = "978-3-16-148410-0", required = true)
    private String isbn;

    @Schema(name = "Price of the book", example = "19.99", required = true)
    private BigDecimal price;

    @Schema(name = "Description of the book", example = "A young wizard's journey")
    private String description;

    @Schema(name = "Cover image URL of the book", example = "https://example.com/cover.jpg")
    private String coverImage;
}
