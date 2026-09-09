package com.boutique.pos.dto;
import lombok.*;
@Getter
@Setter
public class ReturnItemDTO {
    private Long saleItemId;
    private Integer quantity;
    private Long userId;
}
