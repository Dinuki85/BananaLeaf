package com.branchsales.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CentralStockDTO {
    private Double quantity;
    private Double remainingQuantity;
    private Double costPrice;
    private String dealerName;
}
