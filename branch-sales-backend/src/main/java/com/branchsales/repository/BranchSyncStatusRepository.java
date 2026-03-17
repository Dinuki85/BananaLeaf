package com.branchsales.repository;

import com.branchsales.entity.BranchSyncStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchSyncStatusRepository extends JpaRepository<BranchSyncStatus, Long> {
}
