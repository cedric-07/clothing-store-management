package com.boutique.pos.dto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PurchaseItemRequestDTO {
    private Long productVariantId;

    private Integer quantity;

    private BigDecimal purchasePrice;
}
