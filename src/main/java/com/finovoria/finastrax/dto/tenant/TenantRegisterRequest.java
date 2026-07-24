package com.finovoria.finastrax.dto.tenant;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;


@Data
public class TenantRegisterRequest {


    @NotBlank(message = "Bank name is required")
    private String bankName;



    @NotBlank(message = "Bank code is required")
    private String bankCode;



    @NotBlank(message = "Admin name is required")
    private String adminName;



    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;



    @NotBlank(message = "Password is required")
    private String password;

}