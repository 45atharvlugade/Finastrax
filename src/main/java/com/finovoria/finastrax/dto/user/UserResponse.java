package com.finovoria.finastrax.dto.user;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private UUID userId;

    private String username;

    private String fullName;

    private String email;

    private Long tenantId;

    private String role;

    private String status;

}