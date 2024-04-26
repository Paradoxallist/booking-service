package bookingservice.service;

import bookingservice.dto.UserDto;
import bookingservice.dto.UserRegistrationRequestDto;

public interface UserService {
    UserDto register(UserRegistrationRequestDto requestDto);
}
