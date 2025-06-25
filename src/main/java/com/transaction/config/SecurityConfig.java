package com.transaction.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/user").hasAnyAuthority("ROLE_SUPERADMIN", "ROLE_ADMIN")

                        // ✅ NEW ENDPOINTS - for balance
                        .requestMatchers(HttpMethod.POST, "/api/cash-movements/**", "/api/daily-cash-balance/**", "/api/cash-balance/calculate")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPERADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/cash-movements/**", "/api/daily-cash-balance/**")
                        .hasAnyAuthority("ROLE_EMPLOYEE", "ROLE_ADMIN", "ROLE_SUPERADMIN")

                        // ✅ EXISTING ENDPOINTS
                        .requestMatchers("/api/sales/**", "/api/expenses/**")
                        .hasAnyAuthority("ROLE_EMPLOYEE", "ROLE_ADMIN", "ROLE_SUPERADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/user")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPERADMIN")

                        .requestMatchers(HttpMethod.GET,
                                "/api/products/**", "/api/orders/**", "/api/items/**",
                                "/api/expenses/**", "/api/categories/**", "/api/units/**",
                                "/api/addons/**", "/api/flavours/**", "/api/payment-methods/**"
                        ).hasAnyAuthority("ROLE_EMPLOYEE", "ROLE_ADMIN", "ROLE_SUPERADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/orders/**")
                        .hasAnyAuthority("ROLE_EMPLOYEE", "ROLE_ADMIN", "ROLE_SUPERADMIN")

                        .requestMatchers(HttpMethod.POST,
                                "/api/products/**", "/api/payment-methods/**",
                                "/api/categories/**", "/api/units/**",
                                "/api/addons/**", "/api/flavours/**"
                        ).hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPERADMIN")

                        .requestMatchers(HttpMethod.PUT,
                                "/api/products/**", "/api/categories/**",
                                "/api/units/**", "/api/addons/**", "/api/flavours/**"
                        ).hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPERADMIN")

                        .requestMatchers(HttpMethod.DELETE,
                                "/api/products/**", "/api/categories/**",
                                "/api/units/**", "/api/addons/**", "/api/flavours/**"
                        ).hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPERADMIN")
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors().configurationSource(corsConfigurationSource());

        return http.build();
        }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8081", // Expo Web
                "http://localhost:19006")); // Expo App URL (you can add other URLs)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));
        configuration.setAllowCredentials(true); // Allow credentials (cookies, authorization headers)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
