package com.transaction.config;
import com.transaction.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // SecurityConfig.java
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        // public
                        .requestMatchers("/login","/css/**","/js/**").permitAll()
                        // admin‐only user management
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // sales & expenses: allow both employees and admin
                        .requestMatchers("/api/sales/**","/api/expenses/**")
                        .hasAnyRole("EMPLOYEE", "ADMIN")
                        // everything else requires authentication
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .permitAll()
                )
                .logout(logout -> logout.permitAll());
        return http.build();
    }

}
