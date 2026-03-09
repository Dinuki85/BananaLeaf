package com.branchsales.entity;

import java.io.Serializable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceId implements Serializable {
    private Integer idinvoice;
    private Long branchId;
}
