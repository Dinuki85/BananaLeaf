package com.branchsales.dto;

import lombok.Data;
import java.util.List;

@Data
public class BranchProductUpdateRequest {
    private List<BranchProductDTO> updates;
}
