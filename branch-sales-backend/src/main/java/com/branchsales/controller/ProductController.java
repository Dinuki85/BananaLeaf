package com.branchsales.controller;

import com.branchsales.dto.ProductDTO;
import com.branchsales.dto.BranchPriceRequest;
import com.branchsales.entity.MainCategory;
import com.branchsales.repository.MainCategoryRepository;
import com.branchsales.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {
    private final ProductService productService;
    private final MainCategoryRepository mainCategoryRepository;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts(@RequestParam(name = "branchId", required = false) Long branchId) {
        return ResponseEntity.ok(productService.getAllProducts(branchId));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<MainCategory>> getAllCategories() {
        return ResponseEntity.ok(mainCategoryRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @PostMapping("/branch-price")
    public ResponseEntity<Void> updateBranchPrice(@RequestBody BranchPriceRequest request) {
        productService.updateBranchPrice(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/updates")
    public ResponseEntity<List<ProductDTO>> getUpdates(
            @RequestParam(name = "branchId") Long branchId,
            @RequestParam(name = "lastSync") String lastSync) {
        LocalDateTime lastSyncTime = LocalDateTime.parse(lastSync);
        return ResponseEntity.ok(productService.getProductsUpdatedSince(branchId, lastSyncTime));
    }

    @PostMapping("/sync-confirm")
    public ResponseEntity<Void> confirmSync(@RequestBody Map<String, Object> payload) {
        Long branchId = Long.valueOf(payload.get("branchId").toString());
        LocalDateTime syncTime = LocalDateTime.parse(payload.get("syncTime").toString());
        productService.confirmSync(branchId, syncTime);
        return ResponseEntity.ok().build();
    }
}
