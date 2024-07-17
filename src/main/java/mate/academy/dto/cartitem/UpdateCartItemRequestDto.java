package mate.academy.dto.cartitem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Request to update the quantity of a cart item")
public record UpdateCartItemRequestDto(
        @Schema(description = "New quantity of the cart item",
                example = "2", required = true,
                minimum = "1")
        @NotNull @Positive int quantity
) {
}
