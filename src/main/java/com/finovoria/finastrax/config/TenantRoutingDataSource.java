package com.finovoria.finastrax.config;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.finovoria.finastrax.tenant.TenantContext;
import com.finovoria.finastrax.tenant.TenantDatabaseManager;

public class TenantRoutingDataSource implements DataSource {


    private final TenantDatabaseManager tenantDatabaseManager;


    public TenantRoutingDataSource(
            TenantDatabaseManager tenantDatabaseManager
    ) {

        this.tenantDatabaseManager =
                tenantDatabaseManager;
    }



    private DataSource getCurrentDataSource() {


        Long tenantId =
                TenantContext.getTenantId();


        if(tenantId == null) {

            throw new RuntimeException(
                    "Tenant not found in context"
            );
        }


        return tenantDatabaseManager
                .getDataSource(tenantId);
    }



    @Override
    public Connection getConnection()
            throws SQLException {

        return getCurrentDataSource()
                .getConnection();
    }



    @Override
    public Connection getConnection(
            String username,
            String password
    )
            throws SQLException {

        return getCurrentDataSource()
                .getConnection(
                        username,
                        password
                );
    }



    @Override
    public <T> T unwrap(Class<T> iface)
            throws SQLException {

        return getCurrentDataSource()
                .unwrap(iface);
    }



    @Override
    public boolean isWrapperFor(Class<?> iface)
            throws SQLException {

        return getCurrentDataSource()
                .isWrapperFor(iface);
    }



    @Override
    public java.io.PrintWriter getLogWriter()
            throws SQLException {

        return getCurrentDataSource()
                .getLogWriter();
    }



    @Override
    public void setLogWriter(
            java.io.PrintWriter out
    )
            throws SQLException {

        getCurrentDataSource()
                .setLogWriter(out);
    }



    @Override
    public void setLoginTimeout(int seconds)
            throws SQLException {

        getCurrentDataSource()
                .setLoginTimeout(seconds);
    }



    @Override
    public int getLoginTimeout()
            throws SQLException {

        return getCurrentDataSource()
                .getLoginTimeout();
    }



    @Override
    public java.util.logging.Logger getParentLogger()
            throws java.sql.SQLFeatureNotSupportedException {

        return java.util.logging.Logger
                .getLogger("TenantRoutingDataSource");
    }
}