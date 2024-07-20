package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.cartitem.CartItemDto;
import mate.academy.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toDto(CartItem cartItem);

    CartItem toModel(CartItemDto cartItemDto);
}
