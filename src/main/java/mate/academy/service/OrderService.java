package mate.academy.service;

import java.util.List;
import mate.academy.dto.order.OrderDto;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.model.Order;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto createOrder(Long userId, String shippingAddress);

    List<OrderDto> getOrderHistory(Long userId, Pageable pageable);

    List<OrderItemDto> getOrderItems(Long orderId, Pageable pageable);

    OrderItemDto getOrderItem(Long orderId, Long itemId);

    void updateOrderStatus(Long orderId, Order.Status status);
}
