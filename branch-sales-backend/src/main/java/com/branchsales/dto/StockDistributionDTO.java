package com.branchsales.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockDistributionDTO {
    private Long productId;
    private Long branchId;
    private Double quantity;
}
