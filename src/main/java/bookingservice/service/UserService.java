package bookingservice.service;

import bookingservice.dto.UserRegistrationRequestDto;
import bookingservice.dto.UserDto;

public interface UserService {
    UserDto register(UserRegistrationRequestDto requestDto);
}
