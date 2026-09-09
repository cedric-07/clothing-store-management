package com.boutique.pos.dto;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class SaleItemRequestDTO {
    private Long productVariantId;
    private Integer quantity;
    private java.math.BigDecimal discount;
}
