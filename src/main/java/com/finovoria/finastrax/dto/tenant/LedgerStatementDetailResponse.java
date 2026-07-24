package com.finovoria.finastrax.dto.tenant;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LedgerStatementDetailResponse {


    private String ledgerCode;

    private String ledgerName;

    private String groupName;

    private String primaryGroupName;

    private LocalDate transactionDate;

    private BigDecimal openingBalance;

    private BigDecimal debit;

    private BigDecimal credit;

    private BigDecimal closingBalance;

}