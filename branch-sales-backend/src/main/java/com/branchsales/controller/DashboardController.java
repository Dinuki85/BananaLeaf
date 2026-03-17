package com.branchsales.controller;

import com.branchsales.dto.BranchSalesDTO;
import com.branchsales.dto.DashboardSummary;
import com.branchsales.service.DashboardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummary> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/branch-sales")
    public ResponseEntity<List<BranchSalesDTO>> getBranchSales() {
        return ResponseEntity.ok(dashboardService.getAllBranchSales());
    }
}
