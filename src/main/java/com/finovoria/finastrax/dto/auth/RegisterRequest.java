package com.finovoria.finastrax.dto.auth;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String bankName;

    private String bankCode;

    private String adminName;

    private String email;

    private String password;
}