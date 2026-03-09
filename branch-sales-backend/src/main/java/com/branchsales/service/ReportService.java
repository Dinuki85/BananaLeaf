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
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.properties.HorizontalAlignment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class ReportService {

    private final SaleRepository saleRepository;
    private final BranchRepository branchRepository;

    public ReportService(SaleRepository saleRepository, BranchRepository branchRepository) {
        this.saleRepository = saleRepository;
        this.branchRepository = branchRepository;
    }

    public byte[] generateSalesReportPdfByPeriod(Long branchId, String period) throws Exception {
        System.out.println("Generating report for period: " + period);
        java.time.LocalDate today = java.time.LocalDate.now();
        LocalDateTime endDate = today.atTime(23, 59, 59);
        LocalDateTime startDate;

        switch (period.toUpperCase()) {
            case "TODAY":
                startDate = today.atStartOfDay();
                break;
            case "WEEK":
                startDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).atStartOfDay();
                break;
            case "LAST_15_DAYS":
                startDate = today.minusDays(15).atStartOfDay();
                break;
            case "LAST_30_DAYS":
                startDate = today.minusDays(30).atStartOfDay();
                break;
            case "LAST_3_MONTHS":
                startDate = today.minusMonths(3).atStartOfDay();
                break;
            default:
                startDate = today.minusDays(30).atStartOfDay();
        }

        System.out.println("Calculated range: " + startDate + " to " + endDate);
        return generateSalesReportPdf(branchId, startDate, endDate);
    }

    public byte[] generateSalesReportPdf(Long branchId, LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        System.out.println("Fetching sales data from repository...");
        List<Sale> sales = saleRepository.findByFilters(branchId, startDate, endDate);
        System.out.println("Found " + sales.size() + " sales.");
        
        Branch selectedBranch = branchId != null ? branchRepository.findById(branchId).orElse(null) : null;
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        System.out.println("Starting PDF document construction...");

        // Header Section with Logo and Title
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 4}));
        headerTable.setWidth(UnitValue.createPercentValue(100));

        try {
            // First try loading from resources (works in JAR)
            ImageData imageData;
            try {
                ClassPathResource res = new ClassPathResource("static/logo.png");
                imageData = ImageDataFactory.create(res.getURL());
            } catch (Exception e1) {
                // Fallback to project relative path (works in IDE)
                imageData = ImageDataFactory.create("src/main/resources/static/logo.png");
            }
            
            Image logo = new Image(imageData);
            logo.setHeight(60); 
            headerTable.addCell(new Cell().add(logo).setBorder(Border.NO_BORDER));
            System.out.println("Header logo added to PDF.");
        } catch (Exception e) {
            System.err.println("Note: Could not load header logo: " + e.getMessage());
            headerTable.addCell(new Cell().setBorder(Border.NO_BORDER));
        }

        Cell titleCell = new Cell().add(new Paragraph("NEW BANANA LEAF")
                .setFontSize(24)
                .setBold()
                .setTextAlignment(TextAlignment.LEFT))
                .add(new Paragraph("Sales Report")
                .setFontSize(18)
                .setItalic()
                .setTextAlignment(TextAlignment.LEFT))
                .setBorder(Border.NO_BORDER);
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
        int rowCount = 0;
        for (Sale sale : sales) {
            rowCount++;
            System.out.println("Processing sale row " + rowCount + ": " + (sale.getInvoiceLocal() != null ? sale.getInvoiceLocal() : "Unknown"));
            
            // Null-safe Invoice No
            String invoice = sale.getInvoiceLocal() != null ? sale.getInvoiceLocal() : "N/A";
            table.addCell(new Cell().add(new Paragraph(invoice)).setTextAlignment(TextAlignment.CENTER));
            
            // Null-safe Date
            String dateFormatted = "N/A";
            if (sale.getSaleDateTime() != null) {
                try {
                    dateFormatted = sale.getSaleDateTime().format(formatter);
                } catch (Exception e) {
                    System.err.println("Error formatting date for sale " + sale.getId() + ": " + e.getMessage());
                }
            }
            table.addCell(new Cell().add(new Paragraph(dateFormatted)).setTextAlignment(TextAlignment.CENTER));
            
            // Null-safe Branch
            String branchName = (sale.getBranch() != null && sale.getBranch().getName() != null) ? sale.getBranch().getName() : "N/A";
            table.addCell(new Cell().add(new Paragraph(branchName)).setTextAlignment(TextAlignment.CENTER));
            
            // Total Amount
            table.addCell(new Cell().add(new Paragraph(String.format("$%.2f", sale.getTotalAmount()))).setTextAlignment(TextAlignment.RIGHT));
            
            // Null-safe Status
            String status = sale.getStatus() != null ? sale.getStatus() : "N/A";
            table.addCell(new Cell().add(new Paragraph(status)).setTextAlignment(TextAlignment.CENTER));
            
            totalSales += sale.getTotalAmount();
        }

        System.out.println("Finished adding " + rowCount + " rows to table.");
        document.add(table);

        // Summary Section
        document.add(new Paragraph("\n"));
        Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{3, 1}));
        summaryTable.setWidth(UnitValue.createPercentValue(40));
        summaryTable.setHorizontalAlignment(HorizontalAlignment.RIGHT);

        summaryTable.addCell(new Cell().add(new Paragraph("Number of Transactions:").setBold()).setBorder(Border.NO_BORDER));
        summaryTable.addCell(new Cell().add(new Paragraph(String.valueOf(sales.size()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
        
        summaryTable.addCell(new Cell().add(new Paragraph("Total Sales Amount:").setBold().setFontSize(14)).setBorder(Border.NO_BORDER));
        summaryTable.addCell(new Cell().add(new Paragraph(String.format("$%.2f", totalSales)).setBold().setFontSize(14)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));

        document.add(summaryTable);

        // Footer
        document.add(new Paragraph("\n\n\nGenerated by Branch Sales Management System.")
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY));

        // Adding Watermark to Every Page
        try {
            ImageData watermarkData;
            try {
                ClassPathResource res = new ClassPathResource("static/logo.png");
                watermarkData = ImageDataFactory.create(res.getURL());
            } catch (Exception e1) {
                watermarkData = ImageDataFactory.create("src/main/resources/static/logo.png");
            }

            Image watermark = new Image(watermarkData);
            watermark.setWidth(300); // As requested
            watermark.setOpacity(0.15f); // As requested

            int numberOfPages = pdf.getNumberOfPages();
            PdfExtGState gState = new PdfExtGState().setFillOpacity(0.15f);

            for (int i = 1; i <= numberOfPages; i++) {
                PdfPage page = pdf.getPage(i);
                PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamAfter(), page.getResources(), pdf);
                
                float x = page.getPageSize().getWidth() / 2;
                float y = page.getPageSize().getHeight() / 2;
                
                pdfCanvas.saveState();
                pdfCanvas.setExtGState(gState);
                
                Canvas watermarkCanvas = new Canvas(pdfCanvas, page.getPageSize());
                watermarkCanvas.showTextAligned(new Paragraph("").add(watermark), x, y, i, TextAlignment.CENTER, com.itextpdf.layout.properties.VerticalAlignment.MIDDLE, 0);
                watermarkCanvas.close();
                
                pdfCanvas.restoreState();
            }
            System.out.println("Watermark added to " + numberOfPages + " pages.");
        } catch (Exception e) {
            System.err.println("Note: Could not add watermark: " + e.getMessage());
        }

        document.close();

        return baos.toByteArray();
    }
}
