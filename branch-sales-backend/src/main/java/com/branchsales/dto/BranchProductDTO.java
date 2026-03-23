package com.branchsales.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchProductDTO {
    private Long id;
    private Long branchId;
    private String branchName;
    private Long productId;
    private String productName;
    private Double branchPrice;
    private Boolean isAvailable;
    @JsonProperty("isPriceUpdated")
    private Boolean isPriceUpdated;
}
