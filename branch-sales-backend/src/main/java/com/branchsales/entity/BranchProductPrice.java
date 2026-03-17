package com.branchsales.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "branch_product_price", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"product_id", "branch_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchProductPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private MainItem product;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "price")
    private Double price;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    private Long version;
}
