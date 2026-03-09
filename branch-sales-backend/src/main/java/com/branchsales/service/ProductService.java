package com.branchsales.service;

import com.branchsales.dto.ProductDTO;
import com.branchsales.entity.MainItem;
import com.branchsales.repository.MainItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final MainItemRepository mainItemRepository;

    public List<ProductDTO> getAllProducts() {
        return mainItemRepository.findAllWithCategory().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO addProduct(ProductDTO productDTO) {
        MainItem mainItem = MainItem.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .build();
        MainItem saved = mainItemRepository.save(mainItem);
        return convertToDTO(saved);
    }

    private ProductDTO convertToDTO(MainItem item) {
        return ProductDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .category(item.getMainCategory() != null ? item.getMainCategory().getName() : "Uncategorized")
                .build();
    }
}
