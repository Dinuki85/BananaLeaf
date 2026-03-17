package com.branchsales.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incoming_invoice_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncomingInvoiceEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sale_uuid", unique = true, nullable = false, length = 100)
    private String saleUuid;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
