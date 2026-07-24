package com.finovoria.finastrax.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.finovoria.finastrax.tenant.TenantDatabaseManager;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class TenantRoutingDataSourceConfig {

    private final TenantDatabaseManager tenantDatabaseManager;


    @Bean
    public DataSource tenantRoutingDataSource() {

        return new TenantRoutingDataSource(
                tenantDatabaseManager
        );

    }
}