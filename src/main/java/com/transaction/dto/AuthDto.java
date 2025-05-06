package com.transaction.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthDto(
        String username,
        String password,
        String accessToken,
        String refreshToken
) { }
