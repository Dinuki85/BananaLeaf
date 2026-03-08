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

    @GetMapping("/sales/download")
    public ResponseEntity<byte[]> downloadSalesReport(
            @org.springframework.web.bind.annotation.RequestParam(required = false) Long branchId,
            @org.springframework.web.bind.annotation.RequestParam String startDate,
            @org.springframework.web.bind.annotation.RequestParam String endDate) throws Exception {
        
        java.time.LocalDateTime start = java.time.LocalDate.parse(startDate).atStartOfDay();
        java.time.LocalDateTime end = java.time.LocalDate.parse(endDate).atTime(23, 59, 59);

        byte[] pdfContent = reportService.generateSalesReportPdf(branchId, start, end);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "sales_report_" + startDate + "_to_" + endDate + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
    }
}
