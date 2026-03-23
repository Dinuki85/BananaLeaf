package com.branchsales.service;

import com.branchsales.entity.Branch;
import com.branchsales.entity.BranchProduct;
import com.branchsales.entity.MainItem;
import com.branchsales.repository.BranchProductRepository;
import com.branchsales.repository.BranchRepository;
import com.branchsales.repository.MainItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;
    private final MainItemRepository mainItemRepository;
    private final BranchProductRepository branchProductRepository;

    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    @Transactional
    public Branch addBranch(Branch branch) {
        Branch savedBranch = branchRepository.save(branch);
        
        // Create branch_products records for all existing products
        List<MainItem> products = mainItemRepository.findAll();
        for (MainItem product : products) {
            BranchProduct bp = BranchProduct.builder()
                    .branch(savedBranch)
                    .product(product)
                    .branchPrice(product.getSellingPrice())
                    .isAvailable(true)
                    .isPriceUpdated(true)
                    .build();
            branchProductRepository.save(bp);
        }
        
        return savedBranch;
    }

    @Transactional
    public Branch updateBranch(Long id, Branch branchDetails) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));
        
        branch.setName(branchDetails.getName());
        branch.setLocation(branchDetails.getLocation());
        branch.setStatus(branchDetails.getStatus());
        
        return branchRepository.save(branch);
    }
}
