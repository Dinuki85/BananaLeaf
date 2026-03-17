package com.branchsales.service;

import com.branchsales.dto.DashboardSummary;
import com.branchsales.repository.BranchRepository;
import com.branchsales.repository.MainItemRepository;
import com.branchsales.repository.InvoiceRepository;
import com.branchsales.repository.SalesRepository;
import com.branchsales.dto.BranchSalesDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final BranchRepository branchRepository;
    private final MainItemRepository mainItemRepository;
    private final InvoiceRepository invoiceRepository;
    private final SalesRepository salesRepository;

    public DashboardService(BranchRepository branchRepository,
            MainItemRepository mainItemRepository,
            InvoiceRepository invoiceRepository,
            SalesRepository salesRepository) {
        this.branchRepository = branchRepository;
        this.mainItemRepository = mainItemRepository;
        this.invoiceRepository = invoiceRepository;
        this.salesRepository = salesRepository;
    }

    public List<BranchSalesDTO> getAllBranchSales() {
        return salesRepository.getAllBranchSales();
    }

    public DashboardSummary getSummary() {
        Double totalRevenue = invoiceRepository.sumTotalAmount();
        return DashboardSummary.builder()
                .offlineBranches(branchRepository.countByStatus(0))
                .totalProducts(mainItemRepository.count())
                .totalRevenue(totalRevenue != null ? totalRevenue : 0.0)
                .branchSales(invoiceRepository.findBranchPerformance())
                .build();
    }
}
