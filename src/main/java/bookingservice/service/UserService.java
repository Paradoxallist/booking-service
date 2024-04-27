package bookingservice.service;

import bookingservice.dto.UpdateUserRoleRequestDto;
import bookingservice.dto.UserDto;
import bookingservice.dto.UserRegistrationRequestDto;
import bookingservice.dto.UpdateUserRequestDto;
import bookingservice.model.User;

public interface UserService {
    UserDto register(UserRegistrationRequestDto requestDto);

    UserDto getUserInfo(User principal);

    UserDto updateUserInfo(UpdateUserRequestDto requestDto, User principal);

    UserDto updateUserRole(Long id, UpdateUserRoleRequestDto requestDto);
}

