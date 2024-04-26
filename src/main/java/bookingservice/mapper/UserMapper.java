package bookingservice.mapper;

import bookingservice.config.MapperConfig;
import bookingservice.dto.UserDto;
import bookingservice.dto.UserRegistrationRequestDto;
import bookingservice.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserDto toDto(User user);

    User toModel(UserRegistrationRequestDto user);
}
