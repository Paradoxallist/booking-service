package bookingservice.service;

import bookingservice.dto.UpdateUserRequestDto;
import bookingservice.dto.UpdateUserRoleRequestDto;
import bookingservice.dto.UserDto;
import bookingservice.dto.UserRegistrationRequestDto;
import bookingservice.exception.EntityNotFoundException;
import bookingservice.exception.RegistrationException;
import bookingservice.mapper.UserMapper;
import bookingservice.model.Role;
import bookingservice.model.User;
import bookingservice.repository.RoleRepository;
import bookingservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto register(UserRegistrationRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Provided email is already taken");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        user.setRoles(getRoles(Role.RoleName.USER));

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto getUserInfo(User principal) {
        return userMapper.toDto(principal);
    }

    @Override
    public UserDto updateUserInfo(UpdateUserRequestDto requestDto, User principal) {
        userMapper.update(principal, requestDto);
        return userMapper.toDto(userRepository.save(principal));
    }

    @Override
    public UserDto updateUserRole(Long id, UpdateUserRoleRequestDto requestDto) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find user by id: " + id));
        user.setRoles(getRoles(Role.RoleName.valueOf(requestDto.getName())));
        return userMapper.toDto(userRepository.save(user));
    }

    private Set<Role> getRoles(Role.RoleName name) {
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException(name +
                        " role not found. Please check the database."));
        roles.add(userRole);
        return roles;
    }
}
