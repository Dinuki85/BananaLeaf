package com.branchsales.service;

import com.branchsales.entity.Sale;
import com.branchsales.repository.SaleRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ReportService {

    private final SaleRepository saleRepository;

    public ReportService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public byte[] generateSalesReportPdf() {
        List<Sale> sales = saleRepository.findAll();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Centered Title
            document.add(new Paragraph("Branch Sales Management System")
                    .setFontSize(22)
                    .setBold()
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER));

            // Subtitle
            document.add(new Paragraph("Sales Report")
                    .setFontSize(16)
                    .setItalic()
                    .setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER)
                    .setMarginBottom(20));

            float[] columnWidths = { 3, 2, 3, 2, 2 };
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));

            // Table Headers with styling
            table.addHeaderCell(new Paragraph("Branch Name").setBold());
            table.addHeaderCell(new Paragraph("Invoice ID").setBold());
            table.addHeaderCell(new Paragraph("Date and Time").setBold());
            table.addHeaderCell(new Paragraph("Total Amount").setBold());
            table.addHeaderCell(new Paragraph("Status").setBold());

            for (Sale sale : sales) {
                table.addCell(sale.getBranchName() != null ? sale.getBranchName() : "N/A");
                table.addCell(sale.getInvoiceLocal() != null ? sale.getInvoiceLocal() : "N/A");
                table.addCell(sale.getSaleDateTime() != null ? sale.getSaleDateTime().toString() : "N/A");
                table.addCell(String.format("$%.2f", sale.getTotalAmount()));
                table.addCell(sale.getStatus() != null ? sale.getStatus() : "N/A");
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return baos.toByteArray();
    }
}
