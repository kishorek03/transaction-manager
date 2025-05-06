package com.transaction.config;

import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.Key;

@Configuration
public class JwtKeyConfig {

    /**
     * Generates a secure random 256-bit key for signing Access Tokens (HS256).
     */
    @Bean
    public Key accessKey() {
        return Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    }

    /**
     * Generates a secure random 256-bit key for signing Refresh Tokens (HS256).
     */
    @Bean
    public Key refreshKey() {
        return Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);
    }
}
