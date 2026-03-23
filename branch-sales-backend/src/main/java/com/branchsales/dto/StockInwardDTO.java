package com.branchsales.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockInwardDTO {
    private Long productId;
    private Long dealerId;
    private Double quantity;
    private Double costPrice;
}
