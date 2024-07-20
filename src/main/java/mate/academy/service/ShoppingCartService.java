package mate.academy.service;

import mate.academy.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCartForUser(Long userId);

    ShoppingCartDto addToCart(Long userId, Long bookId, int quantity);

    ShoppingCartDto updateCartItemQuantity(Long cartItemId, Long userId, int quantity);

    void removeCartItem(Long cartItemId, Long userId);
}
