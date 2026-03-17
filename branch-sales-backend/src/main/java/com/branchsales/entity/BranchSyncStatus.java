package com.branchsales.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "branch_sync_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchSyncStatus {
    @Id
    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "last_sync_time")
    private LocalDateTime lastSyncTime;
}
