package bookingservice.controller;

import bookingservice.dto.UserDto;
import bookingservice.dto.UpdateUserRequestDto;
import bookingservice.dto.UpdateUserRoleRequestDto;
import bookingservice.model.User;
import bookingservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User management", description = "Endpoints for managing users")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    @Operation(summary = "Get user info", description = "Get profile information for the currently logged-in user")
    public UserDto getUserInfo(Authentication authentication) {
        return userService.getUserInfo((User) authentication.getPrincipal());
    }

    @PatchMapping("/me")
    @Operation(summary = "Update user info", description = "User update their profile information")
    public UserDto updateUser(Authentication authentication,
                              @RequestBody @Valid UpdateUserRequestDto requestDto) {
        return userService.updateUserInfo(requestDto, (User) authentication.getPrincipal());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/role")
    @Operation(summary = "Update user role", description = "Update user role by id")
    public UserDto updateUserRole(@PathVariable("id") Long id,
                              @RequestBody @Valid UpdateUserRoleRequestDto requestDto) {
        return userService.updateUserRole(id, requestDto);
    }
}
