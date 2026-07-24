package com.finovoria.finastrax.dto.tenant;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LedgerMasterResponse {

	private UUID id;

    private String ledgerCode;

    private String ledgerName;

    private String groupName;

    private String primaryGroupName;


}