package com.boutique.pos.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class InventoryDTO {
    private Long id;

    private String inventoryNumber;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime validatedAt;

    private List<InventoryItemDTO> items;
}
