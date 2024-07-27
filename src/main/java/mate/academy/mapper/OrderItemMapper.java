package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.order.OrderItemDto;
import mate.academy.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(source = "orderId", target = "order.id")
    @Mapping(source = "bookId", target = "book.id")
    OrderItem toModel(OrderItemDto orderItemDto);
}
