package com.transaction.controller;

import com.transaction.dto.ApiResponse;
import com.transaction.dto.UserDTO;
import com.transaction.entity.User;
import com.transaction.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<User>> createUser(@RequestBody UserDTO userDTO) {
        log.info("Received request to create user with username: {}", userDTO.username());

        // Perform user creation logic
        User user = userService.createUser(userDTO);
        ApiResponse<User> response = new ApiResponse<>("success", "User created successfully", user);

        log.info("User created successfully with username: {}", user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> listEmployees() {
        log.info("Fetching all users");
        List<UserDTO> dtos = userService.findAllEmployees(); // inject this mapper via constructor
        return ResponseEntity.ok(new ApiResponse<>("success", "Users retrieved", dtos));
    }
}
