package mate.academy.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.OrderItemMapper;
import mate.academy.mapper.OrderMapper;
import mate.academy.model.CartItem;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.repository.order.OrderRepository;
import mate.academy.repository.orderitem.OrderItemRepository;
import mate.academy.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.repository.user.UserRepository;
import mate.academy.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public OrderDto createOrder(Long userId, String shippingAddress) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        if (shoppingCart == null || shoppingCart.getCartItems().isEmpty()) {
            throw new EntityNotFoundException("Shopping cart is empty");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Order order = makeOrder(shippingAddress, user);

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : shoppingCart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            orderItem.setDeleted(false);
            order.getOrderItems().add(orderItem);

            total = total.add(orderItem.getPrice());
        }

        order.setTotal(total);
        orderRepository.save(order);
        orderItemRepository.saveAll(order.getOrderItems());

        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);

        return orderMapper.toDto(order);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderDto> getOrderHistory(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderItemDto> getOrderItems(Long orderId, Pageable pageable) {
        return orderItemRepository.findByOrderId(orderId, pageable).stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public OrderItemDto getOrderItem(Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findByOrderIdAndId(orderId, itemId)
                .orElseThrow(() -> new EntityNotFoundException("Order item not found"));
        return orderItemMapper.toDto(orderItem);
    }

    @Transactional
    @Override
    public void updateOrderStatus(Long orderId, Order.Status status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }

    private static Order makeOrder(String shippingAddress, User user) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(shippingAddress);
        order.setDeleted(false);
        return order;
    }
}
