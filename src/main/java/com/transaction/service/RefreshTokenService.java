package com.transaction.service;

import com.transaction.entity.RefreshToken;
import com.transaction.entity.User;
import com.transaction.repository.RefreshTokenRepository;
import com.transaction.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository; // needed to fetch User entity

    @Transactional
    public void saveRefreshToken(String username, String token) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);

        if (existingToken.isPresent()) {
            RefreshToken refreshToken = existingToken.get();
            refreshToken.setToken(token);
            refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
            refreshTokenRepository.save(refreshToken);
        } else {
            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setUser(user);
            refreshToken.setToken(token);
            refreshToken.setExpiryDate(LocalDateTime.now().plusDays(7));
            refreshTokenRepository.save(refreshToken);
        }
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}
