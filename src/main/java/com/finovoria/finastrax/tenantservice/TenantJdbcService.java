package com.finovoria.finastrax.tenantservice;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.finovoria.finastrax.tenant.TenantContext;
import com.finovoria.finastrax.tenant.TenantDatabaseManager;
import com.finovoria.finastrax.tenantentity.GeneralLedger;
import com.finovoria.finastrax.tenantentity.LedgerMaster;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantJdbcService {

    private final TenantDatabaseManager tenantDatabaseManager;
    private final Map<String, UUID> ledgerCache = new HashMap<>();


    private JdbcTemplate jdbcTemplate() {

        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            throw new RuntimeException("Tenant not found in TenantContext");
        }

        DataSource dataSource =
                tenantDatabaseManager.getDataSource(tenantId);

        return new JdbcTemplate(dataSource);
    }


    private byte[] uuidToBytes(UUID uuid) {

        ByteBuffer buffer = ByteBuffer.allocate(16);

        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());

        return buffer.array();
    }



    public void saveLedgerMasters(List<LedgerMaster> ledgerMasters) {

        if (ledgerMasters == null || ledgerMasters.isEmpty()) {
            return;
        }


        String sql = """
                INSERT IGNORE INTO ledger_master
                (
                    ledger_id,
                    ledger_code,
                    ledger_name,
                    group_name,
                    primary_group_name
                )
                VALUES (?,?,?,?,?)
                """;


        jdbcTemplate().batchUpdate(
                sql,
                ledgerMasters,
                ledgerMasters.size(),
                (ps, ledger) -> {


                    ps.setBytes(
                        1,
                        uuidToBytes(UUID.randomUUID())
                    );


                    ps.setString(
                        2,
                        ledger.getLedgerCode()
                    );


                    ps.setString(
                        3,
                        ledger.getLedgerName()
                    );


                    ps.setString(
                        4,
                        ledger.getGroupName()
                    );


                    ps.setString(
                        5,
                        ledger.getPrimaryGroupName()
                    );

                });


        loadLedgerCache();
    }



    public void saveGeneralLedgers(List<GeneralLedger> generalLedgers) {

        if (generalLedgers == null || generalLedgers.isEmpty()) {
            return;
        }


        String sql = """
                INSERT INTO general_ledger
                (
                    gl_id,
                    ledger_id,
                    ledger_code,
                    ledger_name,
                    group_name,
                    primary_group_name,
                    transaction_date,
                    opening_balance,
                    period_debit,
                    period_credit,
                    closing_balance
                )
                VALUES (?,?,?,?,?,?,?,?,?,?,?)
                """;


        jdbcTemplate().batchUpdate(
                sql,
                generalLedgers,
                generalLedgers.size(),
                (ps, ledger) -> {


                	 UUID ledgerId =
                	            ledgerCache.get(
                	                ledger.getLedgerCode()
                	            );


                	    if (ledgerId == null) {
                	        throw new RuntimeException(
                	            "Ledger not found: "
                	            + ledger.getLedgerCode()
                	        );
                	    }


                	    ps.setBytes(
                	        1,
                	        uuidToBytes(UUID.randomUUID())
                	    );


                    ps.setBytes(
                        2,
                        uuidToBytes(ledgerId)
                    );


                    ps.setString(
                        3,
                        ledger.getLedgerCode()
                    );


                    ps.setString(
                        4,
                        ledger.getLedgerName()
                    );


                    ps.setString(
                        5,
                        ledger.getGroupName()
                    );


                    ps.setString(
                        6,
                        ledger.getPrimaryGroupName()
                    );


                    if(ledger.getTransactionDate()!=null){

                        ps.setDate(
                            7,
                            java.sql.Date.valueOf(
                                ledger.getTransactionDate()
                            )
                        );

                    }else{

                        ps.setNull(
                            7,
                            java.sql.Types.DATE
                        );
                    }


                    ps.setBigDecimal(
                        8,
                        ledger.getOpeningBalance()
                    );


                    ps.setBigDecimal(
                        9,
                        ledger.getPeriodDebit()
                    );


                    ps.setBigDecimal(
                        10,
                        ledger.getPeriodCredit()
                    );


                    ps.setBigDecimal(
                        11,
                        ledger.getClosingBalance()
                    );

                });
    }
    
  
    
    private void loadLedgerCache() {


        ledgerCache.clear();


        List<Map<String,Object>> rows =
                jdbcTemplate().queryForList(
                    """
                    SELECT ledger_id, ledger_code
                    FROM ledger_master
                    """
                );


        for(Map<String,Object> row : rows){


            byte[] bytes =
                    (byte[]) row.get("ledger_id");


            UUID uuid =
                    bytesToUuid(bytes);


            ledgerCache.put(
                (String) row.get("ledger_code"),
                uuid
            );
        }
    }
    public void prepareLedgerCache() {
        ledgerCache.clear();

        List<Map<String,Object>> rows =
                jdbcTemplate().queryForList(
                    """
                    SELECT ledger_id, ledger_code
                    FROM ledger_master
                    """
                );


        for(Map<String,Object> row : rows){

            byte[] bytes =
                    (byte[]) row.get("ledger_id");


            UUID uuid =
                    bytesToUuid(bytes);


            ledgerCache.put(
                (String) row.get("ledger_code"),
                uuid
            );
        }
    }
    private UUID bytesToUuid(byte[] bytes){

        ByteBuffer buffer =
                ByteBuffer.wrap(bytes);


        return new UUID(
            buffer.getLong(),
            buffer.getLong()
        );
    }
}