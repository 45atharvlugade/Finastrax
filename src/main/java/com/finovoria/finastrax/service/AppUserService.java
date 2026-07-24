package com.finovoria.finastrax.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finovoria.finastrax.dto.user.UserRegisterRequest;
import com.finovoria.finastrax.dto.user.UserResponse;
import com.finovoria.finastrax.entity.AppUser;
import com.finovoria.finastrax.entity.Tenant;
import com.finovoria.finastrax.entity.UserStatus;
import com.finovoria.finastrax.repository.AppUserRepository;
import com.finovoria.finastrax.repository.TenantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse registerUser(UserRegisterRequest request) {

        if (appUserRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        if (appUserRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        Tenant tenant = tenantRepository.findById(request.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        AppUser user = AppUser.builder()
                .username(request.getUsername())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .tenant(tenant)
                .role(request.getRole())
                .enabled(true)
                .status(UserStatus.ACTIVE)
                .build();

        AppUser savedUser = appUserRepository.save(user);

        return UserResponse.builder()
                .userId(savedUser.getUserId())
                .username(savedUser.getUsername())
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .tenantId(savedUser.getTenant().getTenantId())
                .role(savedUser.getRole().name())
                .status(savedUser.getStatus().name())
                .build();
    }
}