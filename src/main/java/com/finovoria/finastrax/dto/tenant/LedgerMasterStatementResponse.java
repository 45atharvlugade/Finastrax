package com.finovoria.finastrax.dto.tenant;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LedgerMasterStatementResponse {


    private String ledgerCode;

    private String ledgerName;

    private String groupName;

    private String primaryGroupName;

    private List<LedgerTransactionResponse> statements;

}