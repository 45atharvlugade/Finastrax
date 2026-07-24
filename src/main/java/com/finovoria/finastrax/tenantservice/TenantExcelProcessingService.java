package com.finovoria.finastrax.tenantservice;

import java.io.InputStream;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.finovoria.finastrax.tenantexcel.GeneralLedgerSheetHandler;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TenantExcelProcessingService {


    private final TenantJdbcService tenantJdbcService;



    public void processExcel(MultipartFile file) {

        try {

            IOUtils.setByteArrayMaxOverride(500_000_000);

            OPCPackage opcPackage =
                    OPCPackage.open(
                            file.getInputStream()
                    );

            XSSFReader reader =
                    new XSSFReader(opcPackage);

            StylesTable styles =
                    reader.getStylesTable();

            ReadOnlySharedStringsTable strings =
                    new ReadOnlySharedStringsTable(opcPackage);


            XSSFReader.SheetIterator iterator =
                    (XSSFReader.SheetIterator)
                    reader.getSheetsData();


            while(iterator.hasNext()) {


                try(InputStream sheet = iterator.next()) {


                    GeneralLedgerSheetHandler handler =
                            new GeneralLedgerSheetHandler(
                                    tenantJdbcService
                            );


                    XSSFSheetXMLHandler sheetHandler =
                            new XSSFSheetXMLHandler(
                                    styles,
                                    null,
                                    strings,
                                    handler,
                                    new DataFormatter(),
                                    false
                            );


                    XMLReader sheetParser =
                            XMLHelper.newXMLReader();


                    sheetParser.setContentHandler(sheetHandler);


                    sheetParser.parse(
                            new InputSource(sheet)
                    );

                }

            }


            opcPackage.close();


        }
        catch(Exception e) {

            throw new RuntimeException(
                    "Excel processing failed",
                    e
            );
        }
    }

}