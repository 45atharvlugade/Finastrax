package com.finovoria.finastrax.tenantcontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.finovoria.finastrax.tenant.TenantContext;
import com.finovoria.finastrax.tenantservice.TenantExcelProcessingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tenant/excel")
@RequiredArgsConstructor
public class TenantExcelUploadController {


    private final TenantExcelProcessingService tenantExcelProcessingService;



    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcel(
            @RequestParam("file") MultipartFile file
    ) {


        try {


            /*
             * Temporary testing
             * Later JWT filter will set this automatically
             */
            TenantContext.setTenantId(1008L);



            tenantExcelProcessingService.processExcel(
                    file
            );



            return ResponseEntity.ok(
                    "Excel processed successfully"
            );


        }
        finally {


            TenantContext.clear();

        }

    }

}