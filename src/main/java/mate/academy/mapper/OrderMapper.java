package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.order.OrderDto;
import mate.academy.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderDto toDto(Order order);

    @Mapping(source = "userId", target = "user.id")
    Order toModel(OrderDto orderDto);
}
