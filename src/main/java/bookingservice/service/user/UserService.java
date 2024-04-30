package bookingservice.service.user;

import bookingservice.dto.user.UpdateUserRequestDto;
import bookingservice.dto.user.UpdateUserRoleRequestDto;
import bookingservice.dto.user.UserDto;
import bookingservice.dto.user.UserRegistrationRequestDto;
import bookingservice.model.User;

public interface UserService {
    UserDto register(UserRegistrationRequestDto requestDto);

    UserDto getUserInfo(User principal);

    UserDto updateUserInfo(UpdateUserRequestDto requestDto, User principal);

    UserDto updateUserRole(Long id, UpdateUserRoleRequestDto requestDto);
}

