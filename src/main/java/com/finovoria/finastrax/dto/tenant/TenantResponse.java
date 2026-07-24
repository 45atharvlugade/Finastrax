package com.finovoria.finastrax.dto.tenant;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class TenantResponse {


    private Long tenantId;


    private String tenantCode;


    private String bankName;


    private String bankCode;


    private String databaseName;


    private String status;

}