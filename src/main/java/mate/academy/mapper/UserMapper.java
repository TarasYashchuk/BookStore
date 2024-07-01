package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.user.UserLoginRequestDto;
import mate.academy.dto.user.UserRegistrationRequestDto;
import mate.academy.dto.user.UserResponseDto;
import mate.academy.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toResponseDto(User user);

    User toUser(UserRegistrationRequestDto requestDto);

    User toEntity(UserLoginRequestDto userLoginRequestDto);

    UserLoginRequestDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserLoginRequestDto userLoginRequestDto, @MappingTarget User user);
}
