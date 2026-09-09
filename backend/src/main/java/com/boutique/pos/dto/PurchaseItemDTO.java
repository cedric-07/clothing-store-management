package com.boutique.pos.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class PurchaseItemDTO {
    private Long id;

    private Long productVariantId;

    private String sku;

    private Integer quantityOrdered;

    private Integer quantityReceived;

    private BigDecimal purchasePrice;
}
