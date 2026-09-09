package com.boutique.pos.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReceivePurchaseOrderDTO {
    private Long userId;

    private List<ReceivePurchaseItemDTO> items;
}
