package com.branchsales.controller;

import com.branchsales.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/sales/pdf")
    public ResponseEntity<byte[]> downloadSalesReport() {
        byte[] pdfContent = reportService.generateSalesReportPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "sales_report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
    }
}
