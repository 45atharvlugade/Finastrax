package com.finovoria.finastrax.dto.tenant;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LedgerStatementResponse {

    private LocalDate transactionDate;

    private String ledgerCode;

    private String ledgerName;

    private BigDecimal openingBalance;

    private BigDecimal debit;

    private BigDecimal credit;

    private BigDecimal closingBalance;
}