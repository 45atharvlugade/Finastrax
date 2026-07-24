package com.finovoria.finastrax.tenant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantDatabaseManager {

    @Qualifier("masterDataSource")
    private final DataSource masterDataSource;

    private final Map<Long, DataSource> cache =
            new ConcurrentHashMap<>();


    public DataSource getDataSource(Long tenantId) {

        if (tenantId == null) {
            throw new RuntimeException(
                    "Tenant ID cannot be null"
            );
        }

        return cache.computeIfAbsent(
                tenantId,
                this::createDataSource
        );
    }

    private DataSource createDataSource(Long tenantId) {

        try {

            Connection connection =
                    masterDataSource.getConnection();


            PreparedStatement statement =
                    connection.prepareStatement(
                    """
                    SELECT database_host,
                           database_port,
                           database_name,
                           database_username,
                           database_password
                    FROM tenant
                    WHERE tenant_id = ?
                    """
            );


            statement.setLong(
                    1,
                    tenantId
            );


            ResultSet result =
                    statement.executeQuery();


            if(!result.next()) {

                throw new RuntimeException(
                        "Tenant not found"
                );
            }


            DriverManagerDataSource dataSource =
                    new DriverManagerDataSource();


            dataSource.setDriverClassName(
                    "com.mysql.cj.jdbc.Driver"
            );


            dataSource.setUrl(
                    "jdbc:mysql://"
                    + result.getString("database_host")
                    + ":"
                    + result.getInt("database_port")
                    + "/"
                    + result.getString("database_name")
            );


            dataSource.setUsername(
                    result.getString("database_username")
            );


            dataSource.setPassword(
                    result.getString("database_password")
            );


            result.close();
            statement.close();
            connection.close();


            return dataSource;


        } catch(Exception e) {

            throw new RuntimeException(
                    "Failed to create tenant datasource",
                    e
            );
        }
    }
}