package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);

    @Mapping(target = "user", ignore = true)
    ShoppingCart toEntity(ShoppingCartDto shoppingCartDto);
}
