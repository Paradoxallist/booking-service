package bookingservice.controller;

import bookingservice.dto.user.UpdateUserRequestDto;
import bookingservice.dto.user.UpdateUserRoleRequestDto;
import bookingservice.dto.user.UserDto;
import bookingservice.model.User;
import bookingservice.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User management", description = "Endpoints for managing users")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get user info",
            description = "Get profile information for the currently logged-in user")
    @GetMapping("/me")
    public UserDto getUserInfo(Authentication authentication) {
        return userService.getUserInfo(getUser(authentication));
    }

    @Operation(summary = "Update user info",
            description = "User update their profile information")
    @PutMapping("/me")
    public UserDto updateUser(Authentication authentication,
                              @RequestBody @Valid UpdateUserRequestDto requestDto) {
        return userService.updateUserInfo(requestDto, getUser(authentication));
    }

    @Operation(summary = "Update user role",
            description = "Update user role by id. Allowed for managers only")
    @PreAuthorize("hasAuthority('MANAGER')")
    @PutMapping("/{id}/role")
    public UserDto updateUserRole(@PathVariable("id") Long id,
                              @RequestBody @Valid UpdateUserRoleRequestDto requestDto) {
        return userService.updateUserRole(id, requestDto);
    }

    private User getUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
