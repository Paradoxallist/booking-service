package bookingservice.controller;

import bookingservice.dto.UserRegistrationRequestDto;
import bookingservice.dto.UserDto;
import bookingservice.dto.UserLoginRequestDto;
import bookingservice.dto.UserLoginResponseDto;
import bookingservice.security.AuthenticationService;
import bookingservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authorization", description = "Endpoints for users registration and authorization")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Register user", description = "Create an account")
    @PostMapping("/registration")
    public UserDto registration(@RequestBody @Valid UserRegistrationRequestDto requestDto) {
        return userService.register(requestDto);
    }

    @Operation(summary = "Login user", description = "Login to your account")
    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
