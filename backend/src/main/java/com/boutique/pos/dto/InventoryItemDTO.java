package com.boutique.pos.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InventoryItemDTO {
    private Long id;

    private Long productVariantId;

    private String sku;

    private Integer expectedQuantity;

    private Integer countedQuantity;

    private Integer difference;
}
