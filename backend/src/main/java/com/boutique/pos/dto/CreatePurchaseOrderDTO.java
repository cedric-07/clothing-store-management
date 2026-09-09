package com.boutique.pos.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreatePurchaseOrderDTO {
    private Long supplierId;

    private Long createdBy;

    private List<PurchaseItemRequestDTO> items;
}
