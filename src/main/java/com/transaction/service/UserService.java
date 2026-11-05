package com.transaction.service;

import com.transaction.dto.UserDTO;
import com.transaction.dto.mapper.UserMapper;
import com.transaction.entity.Role;
import com.transaction.entity.User;
import com.transaction.exception.BadRequestException;
import com.transaction.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final UserMapper userMapper;

    public User createUser(UserDTO userDto) {
        if (repo.existsByUsername(userDto.username())) {
            throw new BadRequestException("Username already exists.");
        }

        if (repo.existsByEmail(userDto.email())) {
            throw new BadRequestException("Email already exists.");
        }

        if (repo.existsByMobileNumber(userDto.mobileNumber())) {
            throw new BadRequestException("Mobile number already exists.");
        }

        if (userDto.role() != Role.ROLE_EMPLOYEE && userDto.role() != Role.ROLE_ADMIN) {
            throw new BadRequestException("Only ROLE_EMPLOYEE or ROLE_ADMIN can be assigned.");
        }

        String encodedPassword = encoder.encode(userDto.password());

        User user = User.builder()
                .username(userDto.username())
                .password(encodedPassword)
                .role(userDto.role())
                .mobileNumber(userDto.mobileNumber())
                .email(userDto.email())
                .active(userDto.active() != null ? userDto.active() : true)
                .build();

        return repo.save(user);
    }

    public List<UserDTO> findAllEmployees() {
        return findAllUsers(null, true);
    }

    public List<UserDTO> findAllUsers(String role, Boolean activeOnly) {
        List<User> all = repo.findAll();
        return all.stream()
                .filter(u -> {
                    if (Boolean.TRUE.equals(activeOnly) && u.getActive() != null && !u.getActive()) {
                        return false;
                    }
                    if ("ALL".equalsIgnoreCase(role)) {
                        return true;
                    }
                    if (role != null && !role.isBlank()) {
                        return u.getRole() != null && u.getRole().name().equalsIgnoreCase(role);
                    }
                    boolean hasEmployees = all.stream().anyMatch(user -> user.getRole() == Role.ROLE_EMPLOYEE);
                    if (hasEmployees) {
                        return u.getRole() == Role.ROLE_EMPLOYEE;
                    }
                    return u.getRole() != Role.ROLE_SUPERADMIN;
                })
                .map(userMapper::toDTO)
                .toList();
    }
}
