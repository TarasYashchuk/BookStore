package mate.academy.repository.cartitem;

import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Page<CartItem> findByShoppingCart(ShoppingCart shoppingCart, Pageable pageable);
}
