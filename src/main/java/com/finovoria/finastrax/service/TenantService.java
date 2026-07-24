package com.finovoria.finastrax.service;

import org.springframework.stereotype.Service;

import com.finovoria.finastrax.dto.tenant.TenantRegisterRequest;
import com.finovoria.finastrax.dto.tenant.TenantResponse;
import com.finovoria.finastrax.entity.Tenant;
import com.finovoria.finastrax.entity.TenantStatus;
import com.finovoria.finastrax.repository.TenantRepository;
import com.finovoria.finastrax.tenant.TenantHibernateSchemaGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantService {


    private final TenantRepository tenantRepository;

    private final TenantDatabaseProvisioningService tenantDatabaseProvisioningService;

    private final TenantHibernateSchemaGenerator tenantHibernateSchemaGenerator;



    public TenantResponse registerTenant(
            TenantRegisterRequest request
    ) {


        if (tenantRepository.existsByBankCode(request.getBankCode())) {
            throw new RuntimeException("Bank already registered");
        }


        Tenant tenant = Tenant.builder()
                .bankName(request.getBankName())
                .bankCode(request.getBankCode())
                .status(TenantStatus.PROVISIONING)
                .build();



        // Save first to generate tenantId
        Tenant savedTenant = tenantRepository.save(tenant);



        try {


            // Create database + user + privileges
            savedTenant =
                    tenantDatabaseProvisioningService
                    .provisionDatabase(savedTenant);



            // Save database credentials
            savedTenant =
                    tenantRepository.save(savedTenant);



            // Create tenant tables
            tenantHibernateSchemaGenerator
                    .generateSchema(savedTenant);



            savedTenant.setStatus(
                    TenantStatus.ACTIVE
            );



        } catch(Exception e) {


            savedTenant.setStatus(
                    TenantStatus.FAILED
            );

            tenantRepository.save(savedTenant);


            throw new RuntimeException(
                    "Tenant provisioning failed",
                    e
            );
        }



        savedTenant =
                tenantRepository.save(savedTenant);



        return TenantResponse.builder()
                .tenantId(savedTenant.getTenantId())
                .bankName(savedTenant.getBankName())
                .bankCode(savedTenant.getBankCode())
                .databaseName(savedTenant.getDatabaseName())
                .status(savedTenant.getStatus().name())
                .build();

    }

}