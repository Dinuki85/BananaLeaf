package com.branchsales.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DashboardSummary {
    private long offlineBranches;
    private long totalProducts;
    private double totalRevenue;

    public DashboardSummary() {
    }

    public DashboardSummary(long offlineBranches, long totalProducts, double totalRevenue) {
        this.offlineBranches = offlineBranches;
        this.totalProducts = totalProducts;
        this.totalRevenue = totalRevenue;
    }

    public long getOfflineBranches() {
        return offlineBranches;
    }

    public void setOfflineBranches(long offlineBranches) {
        this.offlineBranches = offlineBranches;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public static DashboardSummaryBuilder builder() {
        return new DashboardSummaryBuilder();
    }

    public static class DashboardSummaryBuilder {
        private long offlineBranches;
        private long totalProducts;
        private double totalRevenue;

        public DashboardSummaryBuilder offlineBranches(long offlineBranches) {
            this.offlineBranches = offlineBranches;
            return this;
        }

        public DashboardSummaryBuilder totalProducts(long totalProducts) {
            this.totalProducts = totalProducts;
            return this;
        }

        public DashboardSummaryBuilder totalRevenue(double totalRevenue) {
            this.totalRevenue = totalRevenue;
            return this;
        }

        public DashboardSummary build() {
            return new DashboardSummary(offlineBranches, totalProducts, totalRevenue);
        }
    }
}
