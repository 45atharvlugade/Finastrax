package com.finovoria.finastrax.dto.user;


import java.util.UUID;

import com.finovoria.finastrax.entity.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;


@Data
public class UserRegisterRequest {


    @NotNull(message = "Tenant id is required")
    private Long tenantId;


    @NotBlank(message = "Username is required")
    private String username;


    @NotBlank(message = "Full name is required")
    private String fullName;


    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;


    @NotBlank(message = "Password is required")
    private String password;


    @NotNull(message = "Role is required")
    private Role role;

}