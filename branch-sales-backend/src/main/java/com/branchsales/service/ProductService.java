package com.branchsales.service;

import com.branchsales.dto.ProductDTO;
import com.branchsales.dto.BranchPriceRequest;
import com.branchsales.entity.MainItem;
import com.branchsales.entity.MainCategory;
import com.branchsales.repository.MainItemRepository;
import com.branchsales.repository.MainCategoryRepository;
import com.branchsales.repository.BranchProductPriceRepository;
import com.branchsales.entity.BranchProductPrice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final MainItemRepository mainItemRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final BranchProductPriceRepository branchProductPriceRepository;

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

        priceRecord.setProduct(product);
        priceRecord.setBranchId(request.getBranchId());
        priceRecord.setPrice(request.getPrice());

        branchProductPriceRepository.save(priceRecord);
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
        return convertToDTO(saved, null);
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