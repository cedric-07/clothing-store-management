package com.boutique.pos.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceivePurchaseItemDTO {
    private Long purchaseItemId;

    private Integer quantityReceived;
}
