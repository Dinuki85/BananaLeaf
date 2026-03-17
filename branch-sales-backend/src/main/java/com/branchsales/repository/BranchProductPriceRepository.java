package com.branchsales.repository;

import com.branchsales.entity.BranchProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BranchProductPriceRepository extends JpaRepository<BranchProductPrice, Long> {
    
    @Query("""
    SELECT bpp FROM BranchProductPrice bpp
    WHERE bpp.branchId = :branchId
    """)
    List<BranchProductPrice> findByBranchId(Long branchId);

    Optional<BranchProductPrice> findByProductIdAndBranchId(Long productId, Long branchId);
}
