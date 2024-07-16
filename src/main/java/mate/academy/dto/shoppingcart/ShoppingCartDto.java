package mate.academy.dto.shoppingcart;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import mate.academy.dto.cartitem.CartItemDto;

@Getter
@Setter
@Schema(description = "Data transfer object for shopping cart")
public class ShoppingCartDto {
    @Schema(description = "ID of the shopping cart", example = "1",
            required = true)
    private Long id;

    @Schema(description = "ID of the user who owns the shopping cart",
            example = "1", required = true)
    private Long userId;

    @Schema(description = "List of cart items in the shopping cart",
            required = true)
    private Set<CartItemDto> cartItems;
}
