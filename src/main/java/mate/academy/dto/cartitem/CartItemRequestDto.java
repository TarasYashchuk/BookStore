package mate.academy.dto.cartitem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Request to add a book to the shopping cart")
public record CartItemRequestDto(
        @Schema(description = "ID of the book to add to the cart",
                example = "1", required = true)
        @NotNull Long bookId,

        @Schema(description = "Quantity of the book to add", example = "2",
                required = true, minimum = "1")
        @NotNull @Positive int quantity
) {
}
