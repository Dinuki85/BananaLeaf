package com.branchsales.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DealerDTO {
    private Long id;
    private String name;
    private String phone;
    private Boolean isActive;
}
