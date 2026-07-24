package com.finovoria.finastrax.tenantentity;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ledger_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LedgerMaster {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ledger_id", nullable = false, updatable = false)
    private UUID ledgerId;

    @Column(name = "ledger_code", nullable = false, unique = true)
    private String ledgerCode;

    @Column(name = "ledger_name", nullable = false)
    private String ledgerName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "primary_group_name")
    private String primaryGroupName;
}