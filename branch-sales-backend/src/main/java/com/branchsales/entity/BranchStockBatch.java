package com.branchsales.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@Entity
@Table(name = "branch_stock_batch")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchStockBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Branch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MainItem product;

    @Column(name = "purchase_price", nullable = false)
    private Double purchasePrice;

    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "remaining_quantity", nullable = false)
    private Double remainingQuantity;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @PrePersist
    protected void onReceive() {
        receivedAt = LocalDateTime.now();
        if (remainingQuantity == null) {
            remainingQuantity = quantity;
        }
    }
}
