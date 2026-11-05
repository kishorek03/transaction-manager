package com.transaction.dto;

import com.transaction.entity.Role;

public record UserDTO(
    Long id,
    String username,
    String password,
    String mobileNumber,
    String email,
    Role role,
    Boolean active
) {}
