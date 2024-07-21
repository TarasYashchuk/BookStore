package mate.academy.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderDto(
        @NotNull Long id,
        @NotNull Long userId,
        @NotBlank String status,
        @NotNull @Positive BigDecimal total,
        @NotNull LocalDateTime orderDate,
        @NotBlank String shippingAddress,
        @NotNull Set<OrderItemDto> orderItems
) {
}
