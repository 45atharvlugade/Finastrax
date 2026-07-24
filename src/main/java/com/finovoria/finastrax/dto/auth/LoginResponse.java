package com.finovoria.finastrax.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String token;

    private String userId;

    private Long tenantId;

    private String role;
}