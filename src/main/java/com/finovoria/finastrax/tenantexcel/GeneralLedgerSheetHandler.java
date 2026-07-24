package com.finovoria.finastrax.tenantexcel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;

import com.finovoria.finastrax.tenantentity.GeneralLedger;
import com.finovoria.finastrax.tenantentity.LedgerMaster;
import com.finovoria.finastrax.tenantservice.TenantJdbcService;

public class GeneralLedgerSheetHandler implements SheetContentsHandler {

    private static final int BATCH_SIZE = 1000;

    private final TenantJdbcService tenantJdbcService;

    private final List<LedgerMaster> ledgerMasterBatch =
            new ArrayList<>();

    private final List<GeneralLedger> generalLedgerBatch =
            new ArrayList<>();

    private final Map<String, Integer> headerMap =
            new HashMap<>();

    private final Map<Integer, String> rowData =
            new HashMap<>();
    
    private final Set<String> processedLedgerCodes =
            new HashSet<>();

    private boolean headerProcessed = false;

    private int currentRow = -1;

    public GeneralLedgerSheetHandler(
            TenantJdbcService tenantJdbcService
    ) {

        this.tenantJdbcService = tenantJdbcService;
    }

    @Override
    public void startRow(int rowNum) {

        currentRow = rowNum;

        rowData.clear();
    }

    @Override
    public void cell(
            String cellReference,
            String formattedValue,
            org.apache.poi.xssf.usermodel.XSSFComment comment
    ) {

        if (cellReference == null) {
            return;
        }

        int columnIndex =
                org.apache.poi.ss.util.CellReference
                        .convertColStringToIndex(
                                cellReference.replaceAll("\\d", "")
                        );

        rowData.put(columnIndex, formattedValue.trim());
    }
    @Override
    public void endRow(int rowNum) {

        // Process Header
        if (!headerProcessed) {

            for (Map.Entry<Integer, String> entry : rowData.entrySet()) {

                headerMap.put(
                        entry.getValue().trim().toUpperCase(),
                        entry.getKey()
                );
            }

            headerProcessed = true;
            return;
        }

        LedgerMaster ledgerMaster = LedgerMaster.builder()
                .ledgerCode(
                        rowData.get(headerMap.get("LEDGER_CODE"))
                )
                .ledgerName(
                        rowData.get(headerMap.get("LEDGER_NAME"))
                )
                .groupName(
                        rowData.get(headerMap.get("GROUP_NAME"))
                )
                .primaryGroupName(
                        rowData.get(headerMap.get("PRIM_GROUP_NAME"))
                )
                .build();

        GeneralLedger generalLedger = GeneralLedger.builder()
                .ledgerCode(
                        rowData.get(headerMap.get("LEDGER_CODE"))
                )
                .ledgerName(
                        rowData.get(headerMap.get("LEDGER_NAME"))
                )
                .groupName(
                        rowData.get(headerMap.get("GROUP_NAME"))
                )
                .primaryGroupName(
                        rowData.get(headerMap.get("PRIM_GROUP_NAME"))
                )
                .transactionDate(
                        parseDate(
                                rowData.get(headerMap.get("TRNDATE"))
                        )
                )
                .openingBalance(
                        parseAmount(
                                rowData.get(headerMap.get("OPENING_BAL"))
                        )
                )
                .periodDebit(
                        parseAmount(
                                rowData.get(headerMap.get("PERIOD_DEBIT"))
                        )
                )
                .periodCredit(
                        parseAmount(
                                rowData.get(headerMap.get("PERIOD_CREDIT"))
                        )
                )
                .closingBalance(
                        parseAmount(
                                rowData.get(headerMap.get("CLOSING_BAL"))
                        )
                )
                .build();
        
        String ledgerCode =
                rowData.get(headerMap.get("LEDGER_CODE"));


        if (processedLedgerCodes.add(ledgerCode)) {

            ledgerMasterBatch.add(ledgerMaster);
        }


        generalLedgerBatch.add(generalLedger);

        if (generalLedgerBatch.size() >= BATCH_SIZE) {

            tenantJdbcService.saveLedgerMasters(
                    ledgerMasterBatch
            );

            tenantJdbcService.saveGeneralLedgers(
                    generalLedgerBatch
            );

            ledgerMasterBatch.clear();
            generalLedgerBatch.clear();
        }
    }

    @Override
    public void endSheet() {

        if (!ledgerMasterBatch.isEmpty()) {

            tenantJdbcService.saveLedgerMasters(
                    ledgerMasterBatch
            );

            ledgerMasterBatch.clear();
        }

        if (!generalLedgerBatch.isEmpty()) {

            tenantJdbcService.saveGeneralLedgers(
                    generalLedgerBatch
            );

            generalLedgerBatch.clear();
        }
    }
    
    private LocalDate parseDate(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        value = value.trim();

        try {

            // Excel format: 4/1/25
            DateTimeFormatter formatter1 =
                    DateTimeFormatter.ofPattern("M/d/yy");

            return LocalDate.parse(value, formatter1);

        } catch (Exception e) {

            try {

                // Format: 04-01-2025
                DateTimeFormatter formatter2 =
                        DateTimeFormatter.ofPattern("dd-MM-yyyy");

                return LocalDate.parse(value, formatter2);

            } catch (Exception ex) {

                try {

                    // Format: 2025-01-04
                    return LocalDate.parse(value);

                } catch (Exception exception) {

                    throw new RuntimeException(
                            "Invalid date format: " + value
                    );
                }
            }
        }
    }

    private BigDecimal parseAmount(String value) {

        if (value == null || value.isBlank()) {
            return BigDecimal.ZERO;
        }

        value = value.replace(",", "").trim();

        return new BigDecimal(value);
    }

}