package com.branchsales.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String sku;
    private String name;
    private Double price;
    private Double sellingPrice;
    private Long categoryId;
    private String category;
    private boolean active;
    private LocalDateTime updatedAt;
    @JsonProperty("branchProducts")
    private List<BranchProductDTO> branchProducts;
    
    @JsonProperty("centralStock")
    private List<CentralStockDTO> centralStock;
}
