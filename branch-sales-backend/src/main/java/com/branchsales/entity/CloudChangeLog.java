package com.branchsales.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cloud_change_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CloudChangeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_name", nullable = false, length = 100)
    private String tableName;

    @Column(name = "record_id", nullable = false)
    private Long recordId;

    @Column(name = "action", nullable = false, length = 20)
    private String action; // INSERT, UPDATE, DELETE

    @Column(name = "data", nullable = false, columnDefinition = "JSON")
    private String data;

    @Column(name = "branch_id")
    private Long branchId; // NULL if global

    @Column(name = "sequence_no", unique = true)
    private Long sequenceNo; 
    // Note: Most JDBC drivers/DBs don't support dual-identity on same table easily.
    // We will rely on 'id' as the sequence for now or use a separate column if specifically required.
    // Given the prompt asks for 'sequence_no', we'll map it to a field.

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
