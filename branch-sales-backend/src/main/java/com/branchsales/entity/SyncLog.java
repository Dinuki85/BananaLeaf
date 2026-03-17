package com.branchsales.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "change_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "branch_id")
    private Long branchId; // NULL if global, specific ID if branch override

    @Column(name = "entity_type")
    private String entityType; // 'product', 'price'

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "operation")
    private String operation; // 'INSERT', 'UPDATE', 'DELETE'

    @Column(name = "version")
    private Long version;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
