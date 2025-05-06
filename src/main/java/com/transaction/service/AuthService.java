package com.transaction.service;

import com.transaction.dto.AuthDto;
import com.transaction.entity.RefreshToken;
import com.transaction.entity.User;
import com.transaction.exception.BadRequestException;
import com.transaction.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    public AuthDto login(String username, String password) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        UserDetails user = (UserDetails) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        refreshTokenService.saveRefreshToken(user.getUsername(), refreshToken);

        return new AuthDto(null, null, accessToken, refreshToken);
    }

    public AuthDto refresh(String refreshToken) {
        RefreshToken tokenEntity = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new BadRequestException("Invalid refresh token"));

        if (tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Refresh token expired");
        }

        User user = tokenEntity.getUser();

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name().substring(5)) // remove "ROLE_"
                .build();

        String newAccessToken = jwtService.generateAccessToken(userDetails);

        return new AuthDto(null, null, newAccessToken, refreshToken);
    }
}
