package com.transaction.dto;

import com.transaction.entity.Role;

public record UserDTO(
    String username,
    String password,
    String mobileNumber,
    String email,
    Role role
) {}
