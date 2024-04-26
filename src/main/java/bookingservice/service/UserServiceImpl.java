package bookingservice.service;

import bookingservice.dto.UserDto;
import bookingservice.dto.UserRegistrationRequestDto;
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
    public UserDto register(UserRegistrationRequestDto requestDto){
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Provided email is already taken");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(Role.RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Default USER role not found. Please check the database."));
        roles.add(userRole);
        user.setRoles(roles);

        return userMapper.toDto(userRepository.save(user));
    }
}
