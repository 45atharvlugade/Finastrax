package com.finovoria.finastrax.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.finovoria.finastrax.tenant.TenantDatabaseManager;
import com.finovoria.finastrax.tenant.TenantContext;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class TenantJdbcTemplateConfig {


    private final TenantDatabaseManager tenantDatabaseManager;


    @Bean
    public JdbcTemplate tenantJdbcTemplate() {


        DataSource dataSource =
                new DataSource() {

                    @Override
                    public java.sql.Connection getConnection()
                            throws java.sql.SQLException {

                        return tenantDatabaseManager
                                .getDataSource(
                                        TenantContext.getTenantId()
                                )
                                .getConnection();
                    }


                    @Override
                    public java.sql.Connection getConnection(
                            String username,
                            String password
                    ) throws java.sql.SQLException {

                        return getConnection();
                    }


                    @Override
                    public <T> T unwrap(Class<T> iface)
                            throws java.sql.SQLException {
                        throw new java.sql.SQLException();
                    }


                    @Override
                    public boolean isWrapperFor(Class<?> iface)
                            throws java.sql.SQLException {
                        return false;
                    }


                    @Override
                    public java.io.PrintWriter getLogWriter()
                            throws java.sql.SQLException {
                        return null;
                    }


                    @Override
                    public void setLogWriter(
                            java.io.PrintWriter out
                    ) throws java.sql.SQLException {
                    }


                    @Override
                    public void setLoginTimeout(int seconds)
                            throws java.sql.SQLException {
                    }


                    @Override
                    public int getLoginTimeout()
                            throws java.sql.SQLException {
                        return 0;
                    }


                    @Override
                    public java.util.logging.Logger getParentLogger() {
                        return java.util.logging.Logger.getGlobal();
                    }
                };


        return new JdbcTemplate(dataSource);
    }
}