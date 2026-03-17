package com.branchsales.service;

import com.branchsales.dto.ProductDTO;
import com.branchsales.dto.BranchPriceRequest;
import com.branchsales.entity.MainItem;
import com.branchsales.entity.MainCategory;
import com.branchsales.repository.MainItemRepository;
import com.branchsales.repository.MainCategoryRepository;
import com.branchsales.repository.BranchProductPriceRepository;
import com.branchsales.repository.SyncLogRepository;
import com.branchsales.repository.BranchSyncStatusRepository;
import com.branchsales.entity.BranchProductPrice;
import com.branchsales.entity.SyncLog;
import com.branchsales.entity.BranchSyncStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final MainItemRepository mainItemRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final BranchProductPriceRepository branchProductPriceRepository;
    private final SyncLogRepository syncLogRepository;
    private final BranchSyncStatusRepository branchSyncStatusRepository;
    private final CloudChangeLogService cloudChangeLogService;

    // Fetch all products with branch pricing logic
    public List<ProductDTO> getAllProducts(Long branchId) {
        List<MainItem> items = mainItemRepository.findAll();
        
        // If branchId is provided, get the branch-specific prices
        Map<Long, Double> branchPrices = new HashMap<>();
        if (branchId != null) {
            branchProductPriceRepository.findByBranchId(branchId)
                .forEach(bpp -> {
                    if (bpp.getProduct() != null) {
                        branchPrices.put(bpp.getProduct().getId(), bpp.getPrice());
                    }
                });
        }

        return items.stream()
                .map(item -> convertToDTO(item, branchPrices.get(item.getId())))
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getAllProducts() {
        return getAllProducts(null);
    }

    @Transactional
    public void updateBranchPrice(BranchPriceRequest request) {
        MainItem product = mainItemRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        BranchProductPrice priceRecord = branchProductPriceRepository
                .findByProductIdAndBranchId(request.getProductId(), request.getBranchId())
                .orElse(new BranchProductPrice());

        // Conflict handling: Server only accepts updates based on the current version
        if (request.getVersion() != null && priceRecord.getVersion() != null 
            && !request.getVersion().equals(priceRecord.getVersion())) {
            throw new RuntimeException("Conflict: Branch price was updated by another sync (Version mismatch)");
        }

        // Increment version manually for branch-specific overrides
        Long nextVersion = (priceRecord.getVersion() != null ? priceRecord.getVersion() : 0L) + 1;

        priceRecord.setProduct(product);
        priceRecord.setBranchId(request.getBranchId());
        priceRecord.setPrice(request.getPrice());
        priceRecord.setVersion(nextVersion);

        BranchProductPrice savedPrice = branchProductPriceRepository.save(priceRecord);
        logSyncRecord("price", product.getId(), request.getBranchId(), "UPDATE", savedPrice.getVersion());
        
        // Phase 1: Cloud Change Log Integration
        cloudChangeLogService.logChange(
            "branch_product_price", 
            savedPrice.getId(), 
            "UPDATE", 
            savedPrice, 
            request.getBranchId()
        );
    }

    // Add product without requiring category from frontend
    public ProductDTO addProduct(ProductDTO productDTO) {
        Double sellingPriceToSave = productDTO.getSellingPrice() != null ? productDTO.getSellingPrice() : productDTO.getPrice();
        Double unitPriceToSave = productDTO.getPrice();

        // Assign default category "Uncategorized"
        MainCategory defaultCategory = mainCategoryRepository.findByName("Uncategorized")
                .orElseGet(() -> {
                    // If it doesn't exist, create one
                    MainCategory newCat = MainCategory.builder()
                            .name("Uncategorized")
                            .build();
                    return mainCategoryRepository.save(newCat);
                });

        MainItem mainItem = MainItem.builder()
                .name(productDTO.getName())
                .sellingPrice(sellingPriceToSave)
                .unitPrice(unitPriceToSave)
                .code(productDTO.getSku())
                .mainCategory(defaultCategory) // always set a category
                .active("1")
                .build();

        MainItem saved = mainItemRepository.save(mainItem);
        logSyncRecord("product", saved.getId(), null, "INSERT", saved.getVersion());

        // Phase 1: Cloud Change Log Integration
        cloudChangeLogService.logChange(
            "main_item", 
            saved.getId(), 
            "INSERT", 
            saved, 
            null
        );

        return convertToDTO(saved, null);
    }

    public List<ProductDTO> getProductsUpdatedSince(Long branchId, LocalDateTime lastSync) {
        List<MainItem> allItems = mainItemRepository.findAll();
        
        // Get branch specific prices for these items
        Map<Long, Double> branchPrices = new HashMap<>();
        branchProductPriceRepository.findByBranchId(branchId)
            .forEach(bpp -> {
                if (bpp.getProduct() != null) {
                    branchPrices.put(bpp.getProduct().getId(), bpp.getPrice());
                }
            });

        return allItems.stream()
                .filter(item -> {
                    // Check if item was updated globally
                    boolean itemUpdated = item.getUpdatedAt() != null && item.getUpdatedAt().isAfter(lastSync);
                    
                    // Check if branch price was updated
                    // We need the branch price record to check its updatedAt
                    Optional<BranchProductPrice> bpp = branchProductPriceRepository.findByProductIdAndBranchId(item.getId(), branchId);
                    boolean priceUpdated = bpp.isPresent() && bpp.get().getUpdatedAt() != null && bpp.get().getUpdatedAt().isAfter(lastSync);
                    
                    return itemUpdated || priceUpdated;
                })
                .map(item -> {
                    ProductDTO dto = convertToDTO(item, branchPrices.get(item.getId()));
                    // Ensure the DTO has the correct updatedAt for sync logic
                    // If the price was the reason for sync, use price's updatedAt
                    Optional<BranchProductPrice> bpp = branchProductPriceRepository.findByProductIdAndBranchId(item.getId(), branchId);
                    if (bpp.isPresent() && bpp.get().getUpdatedAt() != null && (item.getUpdatedAt() == null || bpp.get().getUpdatedAt().isAfter(item.getUpdatedAt()))) {
                        dto.setUpdatedAt(bpp.get().getUpdatedAt());
                    } else {
                        dto.setUpdatedAt(item.getUpdatedAt());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void confirmSync(Long branchId, LocalDateTime syncTime) {
        BranchSyncStatus status = branchSyncStatusRepository.findById(branchId)
                .orElse(new BranchSyncStatus(branchId, syncTime));
        status.setLastSyncTime(syncTime);
        branchSyncStatusRepository.save(status);
    }

    public List<SyncLog> getUpdates(Long sinceVersion, Long branchId) {
        return syncLogRepository.findUpdates(sinceVersion, branchId);
    }

    private void logSyncRecord(String type, Long id, Long branchId, String op, Long version) {
        SyncLog log = SyncLog.builder()
                .entityType(type)
                .entityId(id)
                .branchId(branchId)
                .operation(op)
                .version(version)
                .build();
        syncLogRepository.save(log);
    }

    private ProductDTO convertToDTO(MainItem item, Double branchPrice) {
        String sku = item.getCode() != null ? item.getCode() : (item.getId() != null ? "PROD-" + item.getId() : "N/A");
        
        // Use branch price if exists, otherwise use default sellingPrice
        Double finalSellingPrice = branchPrice != null ? branchPrice : item.getSellingPrice();

        return ProductDTO.builder()
                .id(item.getId())
                .sku(sku)
                .name(item.getName())
                .price(item.getUnitPrice())
                .sellingPrice(finalSellingPrice)
                .active("1".equals(item.getActive()) || "true".equalsIgnoreCase(item.getActive()))
                .category("") // DO NOT RETURN CATEGORY (as requested)
                .build();
    }
}