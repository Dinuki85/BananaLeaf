package com.branchsales.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@Entity
@Table(name = "central_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CentralStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private MainItem product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dealer_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Dealer dealer;

    @Column(name = "cost_price", nullable = false)
    private Double costPrice;

    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "remaining_quantity", nullable = false)
    private Double remainingQuantity;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (remainingQuantity == null) {
            remainingQuantity = quantity;
        }
    }
}
