package com.finovoria.finastrax.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.finovoria.finastrax.entity.Tenant;


@Service
public class TenantDatabaseProvisioningService {


    private final JdbcTemplate masterJdbcTemplate;


    public TenantDatabaseProvisioningService(
            @Qualifier("masterJdbcTemplate") JdbcTemplate masterJdbcTemplate
    ) {
        this.masterJdbcTemplate = masterJdbcTemplate;
    }



    public Tenant provisionDatabase(Tenant tenant) {



        String databaseName =
                "tenant_" + tenant.getTenantId();



        String username =
                "user_" + tenant.getTenantId();



        String password =
                UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0,12);



        // Create Database
        masterJdbcTemplate.execute(
                "CREATE DATABASE IF NOT EXISTS `" 
                + databaseName 
                + "`"
        );



        // Create User
        masterJdbcTemplate.execute(
                "CREATE USER IF NOT EXISTS '"
                + username
                + "'@'localhost' IDENTIFIED BY '"
                + password
                + "'"
        );



        // Reset password if user already exists
        masterJdbcTemplate.execute(
                "ALTER USER '"
                + username
                + "'@'localhost' IDENTIFIED BY '"
                + password
                + "'"
        );



        // Grant access
        masterJdbcTemplate.execute(
                "GRANT ALL PRIVILEGES ON `"
                + databaseName
                + "`.* TO '"
                + username
                + "'@'localhost'"
        );



        masterJdbcTemplate.execute(
                "FLUSH PRIVILEGES"
        );



        tenant.setDatabaseName(databaseName);

        tenant.setDatabaseHost(
                "localhost"
        );

        tenant.setDatabasePort(
                3306
        );

        tenant.setDatabaseUsername(
                username
        );

        tenant.setDatabasePassword(
                password
        );



        return tenant;

    }

}