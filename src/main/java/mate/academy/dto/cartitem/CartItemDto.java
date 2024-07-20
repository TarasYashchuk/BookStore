package mate.academy.dto.cartitem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Data transfer object for cart item")
public class CartItemDto {
    @Schema(description = "ID of the cart item", example = "1",
            required = true)
    @NotNull
    private Long id;

    @Schema(description = "ID of the book in the cart item",
            example = "1", required = true)
    @NotNull
    private Long bookId;

    @Schema(description = "Title of the book in the cart item",
            example = "Effective Java", required = true)
    @NotBlank
    private String bookTitle;

    @Schema(description = "Quantity of the book in the cart item",
            example = "2", required = true, minimum = "1")
    private int quantity;
}
