package com.branchsales.repository;

import com.branchsales.entity.CentralStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CentralStockRepository extends JpaRepository<CentralStock, Long> {
    
    @Query("SELECT cs FROM CentralStock cs WHERE cs.remainingQuantity > 0 ORDER BY cs.createdAt ASC")
    List<CentralStock> findAvailableBatchesOrderByDateAsc();

    List<CentralStock> findByProductIdAndRemainingQuantityGreaterThanOrderByCreatedAtAsc(Long productId, Double minQty);
}
