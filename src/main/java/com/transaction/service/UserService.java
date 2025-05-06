package com.transaction.service;

import com.transaction.dto.UserDTO;
import com.transaction.dto.mapper.UserMapper;
import com.transaction.entity.Role;
import com.transaction.entity.User;
import com.transaction.exception.BadRequestException;
import com.transaction.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
                .build();

        return repo.save(user);
    }

    public List<UserDTO> findAllEmployees() {
        return repo.findAll().stream()
                .filter(u -> u.getRole() == Role.ROLE_EMPLOYEE)
                .map(userMapper::toDTO)
                .toList();
    }
}