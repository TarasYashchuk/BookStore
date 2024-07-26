package mate.academy.dto.order;

import java.math.BigDecimal;

public record OrderItemDto(
        Long id,
        Long orderId,
        Long bookId,
        int quantity,
        BigDecimal price) {
}
