package com.branchsales.service;

import com.branchsales.dto.ProductDTO;
import com.branchsales.entity.MainItem;
import com.branchsales.entity.MainCategory;
import com.branchsales.repository.MainItemRepository;
import com.branchsales.repository.MainCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final MainItemRepository mainItemRepository;
    private final MainCategoryRepository mainCategoryRepository;

    // Fetch all products
    public List<ProductDTO> getAllProducts() {
        return mainItemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Add product without requiring category from frontend
    public ProductDTO addProduct(ProductDTO productDTO) {
        Double priceToSave = productDTO.getPrice() != null ? productDTO.getPrice() : productDTO.getSellingPrice();

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
                .price(priceToSave)
                .code(productDTO.getSku())
                .mainCategory(defaultCategory) // always set a category
                .build();

        MainItem saved = mainItemRepository.save(mainItem);
        return convertToDTO(saved);
    }

    private ProductDTO convertToDTO(MainItem item) {
        String sku = item.getCode() != null ? item.getCode() : (item.getId() != null ? "PROD-" + item.getId() : "N/A");

        return ProductDTO.builder()
                .id(item.getId())
                .sku(sku)
                .name(item.getName())
                .price(item.getPrice())
                .sellingPrice(item.getPrice())
                .active(true)
                // Hide category info from frontend if you want
                //.categoryId(item.getMainCategory() != null ? item.getMainCategory().getId() : null)
                //.category(item.getMainCategory() != null ? item.getMainCategory().getName() : "Uncategorized")
                .build();
    }
}