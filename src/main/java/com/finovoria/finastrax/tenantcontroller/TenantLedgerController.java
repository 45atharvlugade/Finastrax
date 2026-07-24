package com.finovoria.finastrax.tenantcontroller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finovoria.finastrax.dto.tenant.LedgerMasterResponse;
import com.finovoria.finastrax.dto.tenant.LedgerMasterStatementResponse;
import com.finovoria.finastrax.dto.tenant.LedgerStatementDetailResponse;
import com.finovoria.finastrax.tenant.TenantContext;
import com.finovoria.finastrax.tenantservice.TenantLedgerService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/tenant/ledger")
@RequiredArgsConstructor
public class TenantLedgerController {


    private final TenantLedgerService tenantLedgerService;



    @GetMapping("/all")
    public ResponseEntity<List<LedgerMasterResponse>> getAllLedgers(){


        TenantContext.setTenantId(1008L);


        try {

            return ResponseEntity.ok(
                    tenantLedgerService.getAllLedgers()
            );

        }
        finally {

            TenantContext.clear();

        }

    }



    @GetMapping("/master-statement")
    public ResponseEntity<Page<LedgerStatementDetailResponse>> getMasterStatement(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "100") int size

    ) {


        TenantContext.setTenantId(1008L);


        try {


            Pageable pageable =
                    PageRequest.of(
                            page,
                            size
                    );


            return ResponseEntity.ok(
                    tenantLedgerService
                            .getMasterStatement(pageable)
            );


        }
        finally {

            TenantContext.clear();

        }

    }
    
    @GetMapping("/ledger-master-statement")
    public ResponseEntity<Page<LedgerMasterStatementResponse>> getLedgerMasterStatement(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "100") int size

    ) {


        TenantContext.setTenantId(1008L);


        try {


            Pageable pageable =
                    PageRequest.of(
                            page,
                            size
                    );


            return ResponseEntity.ok(
                    tenantLedgerService
                    .getLedgerMasterStatement(
                            pageable
                    )
            );


        }
        finally {


            TenantContext.clear();

        }

    }
    
    @GetMapping("/transaction-report")
    public ResponseEntity<?> getLedgerTransactionReport(

            @RequestParam(required = false)
            String ledgerCode,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "100")
            int size

    ) {

        TenantContext.setTenantId(1008L);

        try {

            Pageable pageable =
                    PageRequest.of(page, size);


            return ResponseEntity.ok(

                tenantLedgerService
                .getLedgerTransactionReport(
                        ledgerCode,
                        fromDate,
                        toDate,
                        pageable
                )

            );

        }
        finally {

            TenantContext.clear();

        }

    }
   
}