package com.branchsales.repository;

import com.branchsales.entity.BranchProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BranchProductRepository extends JpaRepository<BranchProduct, Long> {
    List<BranchProduct> findByBranchId(Long branchId);
    List<BranchProduct> findByProductId(Long productId);
    Optional<BranchProduct> findByBranchIdAndProductId(Long branchId, Long productId);
}
