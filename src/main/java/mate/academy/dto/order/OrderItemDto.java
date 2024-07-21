package mate.academy.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record OrderItemDto(@NotNull Long id,
                           @NotNull Long orderId,
                           @NotNull Long bookId,
                           @Positive int quantity,
                           @Positive BigDecimal price) {
}
