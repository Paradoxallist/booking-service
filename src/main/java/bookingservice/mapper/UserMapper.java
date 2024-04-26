package bookingservice.mapper;

import bookingservice.config.MapperConfig;
import bookingservice.dto.UserRegistrationRequestDto;
import bookingservice.dto.UserDto;
import bookingservice.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserDto toDto(User user);

    User toModel(UserRegistrationRequestDto user);
}
