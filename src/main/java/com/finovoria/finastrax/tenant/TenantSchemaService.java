package com.finovoria.finastrax.tenant;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantSchemaService {

    private final TenantDatabaseManager tenantDatabaseManager;

    public void createTenantSchema(Long tenantId) {

        try {

            DataSource dataSource =
                    tenantDatabaseManager.getDataSource(tenantId);

            Connection connection =
                    dataSource.getConnection();

            Statement statement =
                    connection.createStatement();


            statement.execute("""
                    
                CREATE TABLE IF NOT EXISTS ledger_master (

                    ledger_id BINARY(16) PRIMARY KEY,

                    ledger_code VARCHAR(100) NOT NULL UNIQUE,

                    ledger_name VARCHAR(255) NOT NULL,

                    group_name VARCHAR(100),

                    prim_group_name VARCHAR(100)

                )

            """);


            statement.execute("""
                    
                CREATE TABLE IF NOT EXISTS general_ledger (

                    gl_id BINARY(16) PRIMARY KEY,

                    ledger_id BINARY(16) NOT NULL,

                    trn_date DATE,

                    ledger_code VARCHAR(100),

                    ledger_name VARCHAR(255),

                    group_name VARCHAR(100),

                    prim_group_name VARCHAR(100),

                    opening_bal DECIMAL(19,2),

                    period_debit DECIMAL(19,2),

                    period_credit DECIMAL(19,2),

                    closing_bal DECIMAL(19,2),

                    CONSTRAINT fk_general_ledger_master

                    FOREIGN KEY (ledger_id)

                    REFERENCES ledger_master(ledger_id)

                )

            """);


            statement.close();
            connection.close();


        } catch(Exception e) {

            throw new RuntimeException(
                    "Tenant schema creation failed",
                    e
            );
        }
    }
}