package com.finovoria.finastrax.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tenant")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {

    @Id
    @GeneratedValue(
            strategy = GenerationType.TABLE,
            generator = "tenant_generator"
    )
    @TableGenerator(
            name = "tenant_generator",
            table = "id_generator",
            pkColumnName = "gen_name",
            valueColumnName = "gen_value",
            pkColumnValue = "tenant_id",
            initialValue = 1000,
            allocationSize = 1
    )
    @Column(
            name = "tenant_id",
            nullable = false,
            updatable = false
    )
    private Long tenantId;

    @Column(
            name = "bank_name",
            nullable = false
    )
    private String bankName;

    @Column(
            name = "bank_code",
            nullable = false,
            unique = true
    )
    private String bankCode;

    // Tenant database details

    @Column(
            name = "database_name",
            unique = true
    )
    private String databaseName;

    @Column(name = "database_host")
    private String databaseHost;

    @Column(name = "database_port")
    private Integer databasePort;

    @Column(name = "database_username")
    private String databaseUsername;

    @Column(name = "database_password")
    private String databasePassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TenantStatus status;

    @CreationTimestamp
    @Column(
            name = "created_at",
            updatable = false
    )
    private LocalDateTime createdAt;
}