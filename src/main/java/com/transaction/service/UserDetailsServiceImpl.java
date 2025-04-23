// UserDetailsServiceImpl.java
package com.transaction.service;
import com.transaction.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository repo;
    public UserDetailsServiceImpl(UserRepository repo){this.repo=repo;}

    @Override
    public UserDetails loadUserByUsername(String username) {
        return repo.findByUsername(username)
                .map(u-> User.builder()
                        .username(u.getUsername())
                        .password(u.getPassword())
                        .roles(u.getRole().name().substring(5)) // strips "ROLE_"
                        .build()
                ).orElseThrow(()->new UsernameNotFoundException("Not found"));
    }
}
