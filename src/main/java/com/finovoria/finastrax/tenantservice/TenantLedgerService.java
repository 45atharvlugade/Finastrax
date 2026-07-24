package com.finovoria.finastrax.tenantservice;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.finovoria.finastrax.dto.tenant.LedgerMasterResponse;
import com.finovoria.finastrax.dto.tenant.LedgerMasterStatementResponse;
import com.finovoria.finastrax.dto.tenant.LedgerStatementDetailResponse;
import com.finovoria.finastrax.dto.tenant.LedgerStatementResponse;
import com.finovoria.finastrax.tenantrepository.TenantLedgerRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TenantLedgerService {


    private final TenantLedgerRepository tenantLedgerRepository;



    public List<LedgerMasterResponse> getAllLedgers(){

        return tenantLedgerRepository.findAllLedgers();

    }



    public List<LedgerStatementResponse> getLedgerStatement(
            String ledgerCode
    ){

        return tenantLedgerRepository.getLedgerStatement(
                ledgerCode
        );

    }

    
    public Page<LedgerStatementDetailResponse> getMasterStatement(
            Pageable pageable
    ) {

        return tenantLedgerRepository
                .getMasterStatement(pageable);

    }
    
    
    public Page<LedgerMasterStatementResponse> getLedgerMasterStatement(
            Pageable pageable
    ) {

        return tenantLedgerRepository
                .getLedgerMasterStatement(pageable);

    }
    public Page<LedgerMasterStatementResponse> getLedgerTransactionReport(
            String ledgerCode,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable
    ) {

        return tenantLedgerRepository.getLedgerMasterStatement(
                ledgerCode,
                fromDate,
                toDate,
                pageable
        );
    }
    
}