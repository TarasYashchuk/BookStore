package mate.academy.dto.order;

import jakarta.validation.constraints.NotNull;
import mate.academy.model.Order;

public record UpdateOrderStatusRequestDto(
        @NotNull Order.Status status
) {
}
