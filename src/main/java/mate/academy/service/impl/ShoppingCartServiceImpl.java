package mate.academy.service.impl;

import java.util.HashSet;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.ShoppingCartMapper;
import mate.academy.model.Book;
import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.repository.book.BookRepository;
import mate.academy.repository.cartitem.CartItemRepository;
import mate.academy.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.repository.user.UserRepository;
import mate.academy.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public ShoppingCartDto getShoppingCartForUser(Long userId) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findByUserId(userId));
    }

    @Transactional
    public ShoppingCartDto addToCart(Long userId, Long bookId, int quantity) {
         ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        if (shoppingCart == null) {
            shoppingCart = createNewShoppingCart(userId);
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        Optional<CartItem> existingCartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(bookId))
                .findFirst();

        ShoppingCart finalShoppingCart = shoppingCart;
        existingCartItem.ifPresentOrElse(
                item -> item.setQuantity(item.getQuantity() + quantity),
                () -> addCartItemToCart(finalShoppingCart, book, quantity)
        );

        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    public ShoppingCartDto updateCartItemQuantity(Long cartItemId, Long userId, int quantity) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        if (shoppingCart == null) {
            throw new EntityNotFoundException("Shopping cart not found for user id: " + userId);
        }

        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId,
                        shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Transactional
    public void removeCartItem(Long cartItemId, Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        if (shoppingCart == null) {
            throw new EntityNotFoundException("Shopping cart not found for user id: " + userId);
        }

        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId,
                        shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        shoppingCart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }

    private ShoppingCart createNewShoppingCart(Long userId) {
        ShoppingCart shoppingCart = new ShoppingCart();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        shoppingCart.setUser(user);
        shoppingCart.setCartItems(new HashSet<>());
        shoppingCart.setDeleted(false);
        return shoppingCart;
    }

    private void addCartItemToCart(ShoppingCart shoppingCart, Book book, int quantity) {
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(quantity);
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.getCartItems().add(cartItem);
    }
}
