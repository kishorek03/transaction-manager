package com.transaction.repository;

import com.transaction.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Custom query method to find a user by username
    Optional<User> findByUsername(String username);

    // Custom query method to find a user by email
    Optional<User> findByEmail(String email);

    // Custom query method to find a user by mobile number
    Optional<User> findByMobileNumber(String mobileNumber);

    // Check if a user with a given username exists
    boolean existsByUsername(String username);

    // Check if a user with a given email exists
    boolean existsByEmail(String email);

    // Check if a user with a given mobile number exists
    boolean existsByMobileNumber(String mobileNumber);

    // Find users by their role (if you need to list all users with a specific role)
    List<User> findByRole(String role);

}
