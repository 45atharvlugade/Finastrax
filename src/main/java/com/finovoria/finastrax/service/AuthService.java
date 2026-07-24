package com.finovoria.finastrax.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.finovoria.finastrax.dto.auth.LoginRequest;
import com.finovoria.finastrax.dto.auth.LoginResponse;
import com.finovoria.finastrax.entity.AppUser;
import com.finovoria.finastrax.repository.AppUserRepository;
import com.finovoria.finastrax.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {

        AppUser user = appUserRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        return LoginResponse.builder()
                .token(token)
                .userId(user.getUserId().toString())
                .tenantId(user.getTenant().getTenantId())
                .role(user.getRole().name())
                .build();
    }
}