package com.finovoria.finastrax.dto.tenant;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LedgerTransactionResponse {


    private LocalDate transactionDate;

    private BigDecimal openingBalance;

    private BigDecimal debit;

    private BigDecimal credit;

    private BigDecimal closingBalance;

}