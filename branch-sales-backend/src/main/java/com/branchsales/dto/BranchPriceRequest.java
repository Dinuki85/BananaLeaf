package com.branchsales.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BranchPriceRequest {
    private Long productId;
    private Long branchId;
    private Double price;
}
