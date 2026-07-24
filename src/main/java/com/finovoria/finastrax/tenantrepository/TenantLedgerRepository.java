package com.finovoria.finastrax.tenantrepository;

import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finovoria.finastrax.dto.tenant.LedgerMasterResponse;
import com.finovoria.finastrax.dto.tenant.LedgerMasterStatementResponse;
import com.finovoria.finastrax.dto.tenant.LedgerStatementDetailResponse;
import com.finovoria.finastrax.dto.tenant.LedgerStatementResponse;
import com.finovoria.finastrax.dto.tenant.LedgerTransactionResponse;
import com.finovoria.finastrax.tenant.TenantContext;
import com.finovoria.finastrax.tenant.TenantDatabaseManager;

import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor
public class TenantLedgerRepository {


    private final TenantDatabaseManager tenantDatabaseManager;



    private JdbcTemplate jdbcTemplate(){

        Long tenantId =
                TenantContext.getTenantId();


        if(tenantId == null){
            throw new RuntimeException(
                    "Tenant missing"
            );
        }


        DataSource ds =
                tenantDatabaseManager
                .getDataSource(tenantId);


        return new JdbcTemplate(ds);
    }



    public List<LedgerMasterResponse> findAllLedgers(){


        String sql = """
                SELECT 
                    ledger_id,
                    ledger_code,
                    ledger_name,
                    group_name,
                    primary_group_name
                FROM ledger_master
                """;



        return jdbcTemplate()
                .query(
                    sql,
                    (rs,row)->{


                        byte[] bytes =
                                rs.getBytes("ledger_id");


                        UUID id =
                                bytesToUuid(bytes);



                        return new LedgerMasterResponse(

                                id,

                                rs.getString("ledger_code"),

                                rs.getString("ledger_name"),

                                rs.getString("group_name"),

                                rs.getString("primary_group_name")
                        );
                    }
                );
    }




    public List<LedgerStatementResponse> getLedgerStatement(
            String ledgerCode
    ){


        String sql="""
                SELECT
                    transaction_date,
                    ledger_code,
                    ledger_name,
                    opening_balance,
                    period_debit,
                    period_credit,
                    closing_balance

                FROM general_ledger

                WHERE ledger_code = ?

                ORDER BY transaction_date
                """;


        return jdbcTemplate()
                .query(
                    sql,
                    (rs,row)->{


                        return new LedgerStatementResponse(

                            rs.getDate(
                                "transaction_date"
                            )
                            .toLocalDate(),


                            rs.getString(
                                "ledger_code"
                            ),


                            rs.getString(
                                "ledger_name"
                            ),


                            rs.getBigDecimal(
                                "opening_balance"
                            ),


                            rs.getBigDecimal(
                                "period_debit"
                            ),


                            rs.getBigDecimal(
                                "period_credit"
                            ),


                            rs.getBigDecimal(
                                "closing_balance"
                            )

                        );
                    },
                    ledgerCode
                );

    }



    private UUID bytesToUuid(byte[] bytes){

        ByteBuffer buffer =
                ByteBuffer.wrap(bytes);


        return new UUID(
                buffer.getLong(),
                buffer.getLong()
        );
    }
    public Page<LedgerStatementDetailResponse> getMasterStatement(
            Pageable pageable
    ) {


        String sql = """

            SELECT

            lm.ledger_code,
            lm.ledger_name,
            lm.group_name,
            lm.primary_group_name,

            gl.transaction_date,
            gl.opening_balance,
            gl.period_debit,
            gl.period_credit,
            gl.closing_balance


            FROM ledger_master lm


            JOIN general_ledger gl

            ON lm.ledger_id = gl.ledger_id


            ORDER BY 
            gl.transaction_date,
            lm.ledger_code


            LIMIT ? OFFSET ?

            """;


        List<LedgerStatementDetailResponse> content =
                jdbcTemplate()
                .query(

                    sql,

                    (rs,row)-> new LedgerStatementDetailResponse(

                        rs.getString("ledger_code"),

                        rs.getString("ledger_name"),

                        rs.getString("group_name"),

                        rs.getString("primary_group_name"),

                        rs.getDate("transaction_date")
                           .toLocalDate(),

                        rs.getBigDecimal("opening_balance"),

                        rs.getBigDecimal("period_debit"),

                        rs.getBigDecimal("period_credit"),

                        rs.getBigDecimal("closing_balance")

                    ),

                    pageable.getPageSize(),

                    pageable.getOffset()
                );


        Long total =
                jdbcTemplate()
                .queryForObject(

                    """
                    SELECT COUNT(*)

                    FROM general_ledger
                    """,

                    Long.class
                );


        return new PageImpl<>(
                content,
                pageable,
                total
        );
    }
    
    public Page<LedgerMasterStatementResponse> getLedgerMasterStatement(
            Pageable pageable
    ) {


        String sql = """

            SELECT

                ledger_code,
                ledger_name,
                group_name,
                primary_group_name

            FROM ledger_master

            ORDER BY ledger_code

            LIMIT ? OFFSET ?

            """;


        List<LedgerMasterStatementResponse> content =
                jdbcTemplate()
                .query(

                    sql,

                    (rs, row) -> {


                        String ledgerCode =
                                rs.getString("ledger_code");


                        return new LedgerMasterStatementResponse(

                                ledgerCode,

                                rs.getString("ledger_name"),

                                rs.getString("group_name"),

                                rs.getString("primary_group_name"),


                                getLedgerTransactions(
                                        ledgerCode
                                )

                        );

                    },

                    pageable.getPageSize(),

                    pageable.getOffset()

                );



        Long total =
                jdbcTemplate()
                .queryForObject(

                    """
                    SELECT COUNT(*)
                    FROM ledger_master
                    """,

                    Long.class

                );



        return new PageImpl<>(
                content,
                pageable,
                total
        );

    }
    
    public List<LedgerTransactionResponse> getLedgerTransactions(
            String ledgerCode
    ) {


        String sql = """

            SELECT

                transaction_date,
                opening_balance,
                period_debit,
                period_credit,
                closing_balance

            FROM general_ledger

            WHERE ledger_code = ?

            ORDER BY transaction_date

            """;


        return jdbcTemplate()
                .query(

                    sql,

                    (rs,row) -> new LedgerTransactionResponse(

                        rs.getDate("transaction_date")
                          .toLocalDate(),

                        rs.getBigDecimal("opening_balance"),

                        rs.getBigDecimal("period_debit"),

                        rs.getBigDecimal("period_credit"),

                        rs.getBigDecimal("closing_balance")

                    ),

                    ledgerCode

                );

    }
    
    public Page<LedgerMasterStatementResponse> getLedgerMasterStatement(
            String ledgerCode,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable
    ) {


        StringBuilder sql = new StringBuilder("""
                
            SELECT DISTINCT
            
                lm.ledger_code,
                lm.ledger_name,
                lm.group_name,
                lm.primary_group_name
                
            FROM ledger_master lm
                
            JOIN general_ledger gl
                
            ON lm.ledger_id = gl.ledger_id
                
            WHERE 1=1
                
            """);


        List<Object> params = new ArrayList<>();


        if (ledgerCode != null && !ledgerCode.isBlank()) {

            sql.append(" AND lm.ledger_code = ? ");

            params.add(ledgerCode);

        }


        if (fromDate != null && toDate != null) {

            sql.append("""
                    AND gl.transaction_date 
                    BETWEEN ? AND ?
                    """);

            params.add(fromDate);
            params.add(toDate);

        }
        else if (fromDate != null) {


            sql.append("""
                    AND gl.transaction_date = ?
                    """);

            params.add(fromDate);

        }



        sql.append("""
                
                ORDER BY lm.ledger_code
                
                LIMIT ? OFFSET ?
                
                """);


        params.add(pageable.getPageSize());

        params.add(pageable.getOffset());



        List<LedgerMasterStatementResponse> content =
                jdbcTemplate()
                .query(

                    sql.toString(),

                    (rs,row)->{


                        String code =
                                rs.getString("ledger_code");


                        return new LedgerMasterStatementResponse(

                            code,

                            rs.getString("ledger_name"),

                            rs.getString("group_name"),

                            rs.getString("primary_group_name"),


                            getLedgerTransactions(
                                    code,
                                    fromDate,
                                    toDate
                            )

                        );

                    },

                    params.toArray()

                );



        return new PageImpl<>(
                content,
                pageable,
                content.size()
        );

    }
    
    public List<LedgerTransactionResponse> getLedgerTransactions(
            String ledgerCode,
            LocalDate fromDate,
            LocalDate toDate
    ) {


        StringBuilder sql = new StringBuilder("""
                
            SELECT
                
                transaction_date,
                opening_balance,
                period_debit,
                period_credit,
                closing_balance
                
            FROM general_ledger
                
            WHERE ledger_code = ?
                
            """);


        List<Object> params = new ArrayList<>();

        params.add(ledgerCode);



        if(fromDate != null && toDate != null){


            sql.append("""
                    AND transaction_date BETWEEN ? AND ?
                    """);


            params.add(fromDate);
            params.add(toDate);


        }
        else if(fromDate != null){


            sql.append("""
                    AND transaction_date = ?
                    """);


            params.add(fromDate);

        }



        sql.append("""
                
                ORDER BY transaction_date
                
                """);



        return jdbcTemplate()
                .query(

                    sql.toString(),

                    (rs,row)->new LedgerTransactionResponse(

                        rs.getDate("transaction_date")
                          .toLocalDate(),

                        rs.getBigDecimal("opening_balance"),

                        rs.getBigDecimal("period_debit"),

                        rs.getBigDecimal("period_credit"),

                        rs.getBigDecimal("closing_balance")

                    ),

                    params.toArray()

                );

    }
    
    
}