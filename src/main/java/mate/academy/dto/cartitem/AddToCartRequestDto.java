package mate.academy.dto.cartitem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to add a book to the shopping cart")
public record AddToCartRequestDto(
        @Schema(description = "ID of the book to add to the cart",
                example = "1", required = true)
        @NotNull Long bookId,

        @Schema(description = "Quantity of the book to add", example = "2",
                required = true, minimum = "1")
        @NotNull @Min(1) int quantity
) {
}
