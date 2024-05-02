package bookingservice.mapper;

import bookingservice.config.MapperConfig;
import bookingservice.dto.user.UpdateUserRequestDto;
import bookingservice.dto.user.UserDto;
import bookingservice.dto.user.UserRegistrationRequestDto;
import bookingservice.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserDto toDto(User user);

    User toModel(UserRegistrationRequestDto user);

    void update(@MappingTarget User user, UpdateUserRequestDto requestDto);

    @Named("idByUser")
    default Long idByUser(User user) {
        return user.getId();
    }
}
