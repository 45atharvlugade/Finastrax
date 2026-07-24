package com.finovoria.finastrax.tenantentity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "general_ledger")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneralLedger {

    @Id
    @Column(name = "gl_id", columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    private UUID glId;

    @Column(name = "ledger_code", nullable = false)
    private String ledgerCode;

    @Column(name = "ledger_name", nullable = false)
    private String ledgerName;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "primary_group_name")
    private String primaryGroupName;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    @Column(name = "opening_balance", precision = 19, scale = 2)
    private BigDecimal openingBalance;

    @Column(name = "period_debit", precision = 19, scale = 2)
    private BigDecimal periodDebit;

    @Column(name = "period_credit", precision = 19, scale = 2)
    private BigDecimal periodCredit;

    @Column(name = "closing_balance", precision = 19, scale = 2)
    private BigDecimal closingBalance;
}