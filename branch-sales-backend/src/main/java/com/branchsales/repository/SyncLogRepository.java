package com.branchsales.repository;

import com.branchsales.entity.SyncLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SyncLogRepository extends JpaRepository<SyncLog, Long> {
    
    @Query("""
    SELECT s FROM SyncLog s 
    WHERE s.version > :sinceVersion 
    AND (s.branchId IS NULL OR s.branchId = :branchId)
    ORDER BY s.version ASC
    """)
    List<SyncLog> findUpdates(Long sinceVersion, Long branchId);

    @Query("SELECT MAX(s.version) FROM SyncLog s")
    Long findMaxVersion();
}
