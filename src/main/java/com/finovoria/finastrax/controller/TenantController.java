package com.finovoria.finastrax.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finovoria.finastrax.dto.tenant.TenantRegisterRequest;
import com.finovoria.finastrax.dto.tenant.TenantResponse;
import com.finovoria.finastrax.service.TenantService;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {



    private final TenantService tenantService;




    @PostMapping("/register")
    public ResponseEntity<TenantResponse> registerTenant(
            @RequestBody TenantRegisterRequest request
    ) {


        TenantResponse response =
                tenantService.registerTenant(request);



        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

}