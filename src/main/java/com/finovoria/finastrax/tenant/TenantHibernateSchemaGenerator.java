package com.finovoria.finastrax.tenant;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Service;

import com.finovoria.finastrax.entity.Tenant;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantHibernateSchemaGenerator {

    private final TenantDatabaseManager tenantDatabaseManager;

    public void generateSchema(Tenant tenant) {

        DataSource dataSource =
                tenantDatabaseManager.getDataSource(
                        tenant.getTenantId()
                );

        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();

        factory.setDataSource(dataSource);

        // Scan ONLY tenant entities
        factory.setPackagesToScan(
                "com.finovoria.finastrax.tenantentity"
        );

        HibernateJpaVendorAdapter vendorAdapter =
                new HibernateJpaVendorAdapter();

        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(false);

        factory.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();

        properties.put(
                "hibernate.hbm2ddl.auto",
                "update"
        );

        properties.put(
                "hibernate.dialect",
                "org.hibernate.dialect.MySQLDialect"
        );

        properties.put(
                "hibernate.show_sql",
                false
        );

        properties.put(
                "hibernate.format_sql",
                true
        );

        properties.put(
                "hibernate.jdbc.lob.non_contextual_creation",
                true
        );

        factory.setJpaPropertyMap(properties);

        factory.afterPropertiesSet();

        EntityManagerFactory entityManagerFactory =
                factory.getObject();

        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}