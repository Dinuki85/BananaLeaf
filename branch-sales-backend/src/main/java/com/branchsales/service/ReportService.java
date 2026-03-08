package com.branchsales.service;

import com.branchsales.entity.Branch;
import com.branchsales.entity.Sale;
import com.branchsales.repository.BranchRepository;
import com.branchsales.repository.SaleRepository;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReportService {

    private final SaleRepository saleRepository;
    private final BranchRepository branchRepository;

    public ReportService(SaleRepository saleRepository, BranchRepository branchRepository) {
        this.saleRepository = saleRepository;
        this.branchRepository = branchRepository;
    }

    public byte[] generateSalesReportPdf(Long branchId, LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        List<Sale> sales = saleRepository.findByFilters(branchId, startDate, endDate);
        Branch selectedBranch = branchId != null ? branchRepository.findById(branchId).orElse(null) : null;
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Header Section with Logo and Title
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 4}));
        headerTable.setWidth(UnitValue.createPercentValue(100));

        try {
            ClassPathResource res = new ClassPathResource("static/images/logo.png");
            ImageData imageData = ImageDataFactory.create(res.getURL());
            Image logo = new Image(imageData);
            logo.setWidth(80);
            headerTable.addCell(new Cell().add(logo).setBorder(com.itextpdf.layout.properties.Border.NO_BORDER));
        } catch (Exception e) {
            headerTable.addCell(new Cell().add(new Paragraph("Logo")).setBorder(com.itextpdf.layout.properties.Border.NO_BORDER));
        }

        Cell titleCell = new Cell().add(new Paragraph("NEW BANANA LEAF")
                .setFontSize(24)
                .setBold()
                .setTextAlignment(TextAlignment.LEFT))
                .add(new Paragraph("Sales Report")
                .setFontSize(18)
                .setItalic()
                .setTextAlignment(TextAlignment.LEFT))
                .setBorder(com.itextpdf.layout.properties.Border.NO_BORDER)
                .setVerticalAlignment(com.itextpdf.layout.properties.VerticalAlignment.MIDDLE);
        headerTable.addCell(titleCell);
        document.add(headerTable);

        document.add(new Paragraph("\n"));

        // Report Info
        document.add(new Paragraph("Branch: " + (selectedBranch != null ? selectedBranch.getName() : "All Branches"))
                .setFontSize(12).setBold());
        document.add(new Paragraph("Period: " + startDate.format(dateOnlyFormatter) + " to " + endDate.format(dateOnlyFormatter))
                .setFontSize(12).setBold());
        document.add(new Paragraph("Generated Date: " + LocalDateTime.now().format(formatter))
                .setFontSize(10).setItalic().setMarginBottom(20));

        // Data Table
        float[] columnWidths = { 2, 3, 3, 2, 2 };
        Table table = new Table(UnitValue.createPercentArray(columnWidths));
        table.setWidth(UnitValue.createPercentValue(100));

        // Table Headers
        String[] headers = {"Invoice No", "Date", "Branch", "Total Amount", "Status"};
        for (String header : headers) {
            table.addHeaderCell(new Cell().add(new Paragraph(header).setBold())
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER));
        }

        double totalSales = 0;
        for (Sale sale : sales) {
            table.addCell(new Cell().add(new Paragraph(sale.getInvoiceLocal() != null ? sale.getInvoiceLocal() : "N/A")).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph(sale.getSaleDateTime().format(formatter))).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph(sale.getBranch() != null ? sale.getBranch().getName() : "N/A")).setTextAlignment(TextAlignment.CENTER));
            table.addCell(new Cell().add(new Paragraph(String.format("$%.2f", sale.getTotalAmount()))).setTextAlignment(TextAlignment.RIGHT));
            table.addCell(new Cell().add(new Paragraph(sale.getStatus())).setTextAlignment(TextAlignment.CENTER));
            totalSales += sale.getTotalAmount();
        }

        document.add(table);

        // Summary Section
        document.add(new Paragraph("\n"));
        Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{3, 1}));
        summaryTable.setWidth(UnitValue.createPercentValue(40));
        summaryTable.setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.RIGHT);

        summaryTable.addCell(new Cell().add(new Paragraph("Number of Transactions:").setBold()).setBorder(com.itextpdf.layout.properties.Border.NO_BORDER));
        summaryTable.addCell(new Cell().add(new Paragraph(String.valueOf(sales.size()))).setTextAlignment(TextAlignment.RIGHT).setBorder(com.itextpdf.layout.properties.Border.NO_BORDER));
        
        summaryTable.addCell(new Cell().add(new Paragraph("Total Sales Amount:").setBold().setFontSize(14)).setBorder(com.itextpdf.layout.properties.Border.NO_BORDER));
        summaryTable.addCell(new Cell().add(new Paragraph(String.format("$%.2f", totalSales)).setBold().setFontSize(14)).setTextAlignment(TextAlignment.RIGHT).setBorder(com.itextpdf.layout.properties.Border.NO_BORDER));

        document.add(summaryTable);

        // Footer
        document.add(new Paragraph("\n\n\nGenerated by Branch Sales Management System.")
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY));

        document.close();

        return baos.toByteArray();
    }
}
