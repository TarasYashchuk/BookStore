package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.cartitem.AddToCartRequestDto;
import mate.academy.dto.cartitem.CartItemDto;
import mate.academy.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.model.User;
import mate.academy.service.ShoppingCartService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping Cart Management", description = "Endpoints for managing shopping carts")
@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "Get shopping cart",
            description = "Retrieve the shopping cart for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved shopping cart"),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized")
    })
    @GetMapping
    public ShoppingCartDto getCart(@AuthenticationPrincipal User user) {
        Long userId = user.getId();
        return shoppingCartService.getShoppingCartForUser(userId);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "Add to cart", description = "Add a book to the shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added to cart"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ShoppingCartDto addToCart(@AuthenticationPrincipal User user,
                                     @RequestBody @Valid AddToCartRequestDto request) {
        Long userId = user.getId();
        return shoppingCartService.addToCart(userId, request.bookId(), request.quantity());
    }

    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "Update cart item quantity",
            description = "Update the quantity of a cart item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated cart item"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Cart item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/items/{cartItemId}")
    public ShoppingCartDto updateCartItemQuantity(@PathVariable @Parameter
            (description = "ID of the cart item to be updated", required = true)
                                                      Long cartItemId,
                                                  @RequestBody
                                                  @Valid UpdateCartItemRequestDto request) {
        return shoppingCartService.updateCartItemQuantity(cartItemId, request.quantity());
    }

    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "Remove cart item", description = "Remove an item from the shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully removed cart item"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Cart item not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/items/{cartItemId}")
    public void removeCartItem(@PathVariable @Parameter
            (description = "ID of the cart item to be removed", required = true) Long cartItemId) {
        shoppingCartService.removeCartItem(cartItemId);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "Get cart items with pagination and sorting",
            description = "Retrieve the list of cart items for the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved cart items"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/items")
    public List<CartItemDto> getCartItems(
            @AuthenticationPrincipal User user,
            @ParameterObject @PageableDefault Pageable pageable) {
        Long userId = user.getId();
        return shoppingCartService.getCartItems(userId, pageable);
    }
}
