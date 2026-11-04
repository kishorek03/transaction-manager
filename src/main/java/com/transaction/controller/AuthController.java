package com.transaction.controller;

import com.transaction.dto.ApiResponse;
import com.transaction.dto.AuthDto;
import com.transaction.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    // AuthController.java
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthDto>> login(@RequestBody AuthDto req) {
        AuthDto response = authService.login(req.username(), req.password());
        return ResponseEntity.ok(new ApiResponse<>("success", "Login successful", response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthDto>> refresh(@RequestBody AuthDto req) {
        AuthDto response = authService.refresh(req.refreshToken());
        return ResponseEntity.ok(new ApiResponse<>("success", "Token refreshed", response));
    }
}
