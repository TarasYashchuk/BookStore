package mate.academy.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.model.Order;

@Data
public class UpdateOrderStatusRequestDto {
    @NotNull
    private Order.Status status;
}
