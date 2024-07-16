package mate.academy.service;

import java.util.List;
import mate.academy.dto.cartitem.CartItemDto;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import org.springframework.data.domain.Pageable;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCartForUser(Long userId);

    ShoppingCart createShoppingCart(User user);

    ShoppingCartDto addToCart(Long userId, Long bookId, int quantity);

    ShoppingCartDto updateCartItemQuantity(Long cartItemId, int quantity);

    List<CartItemDto> getCartItems(Long userId, Pageable pageable);

    void removeCartItem(Long cartItemId);
}
