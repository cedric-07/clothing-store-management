package com.boutique.pos.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountInventoryItemDTO {
    private Long productVariantId;

    private Integer countedQuantity;
}
