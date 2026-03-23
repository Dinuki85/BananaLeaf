package com.branchsales.controller;

import com.branchsales.dto.BranchProductUpdateRequest;
import com.branchsales.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/branch-products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class BranchProductController {
    private final ProductService productService;

    @PutMapping("/update")
    public ResponseEntity<Void> updateBranchProducts(@RequestBody BranchProductUpdateRequest request) {
        productService.updateBranchProducts(request);
        return ResponseEntity.ok().build();
    }
}
