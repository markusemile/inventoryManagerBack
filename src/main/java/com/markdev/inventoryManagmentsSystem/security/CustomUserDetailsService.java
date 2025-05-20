package com.markdev.inventoryManagmentsSystem.security;

import com.markdev.inventoryManagmentsSystem.entity.User;
import com.markdev.inventoryManagmentsSystem.exceptions.NotFoundException;
import com.markdev.inventoryManagmentsSystem.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findUserByEmail(username).orElseThrow(()->new NotFoundException("User Email not found"));
        return AuthUser.builder()
                .user(user)
                .build();
    }
}
