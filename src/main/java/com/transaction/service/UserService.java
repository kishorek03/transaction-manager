package com.transaction.service;

import com.transaction.entity.Role;
import com.transaction.entity.User;
import com.transaction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    @Autowired private UserRepository repo;
    @Autowired private PasswordEncoder encoder;

    public User createEmployee(String username, String rawPassword) {
        User u = User.builder()
                .username(username)
                .password(encoder.encode(rawPassword))
                .role(Role.ROLE_EMPLOYEE)
                .build();
        return repo.save(u);
    }
    public List<User> findAllEmployees() {
        return repo.findAll().stream()
                .filter(u->u.getRole()==Role.ROLE_EMPLOYEE).toList();
    }
}
