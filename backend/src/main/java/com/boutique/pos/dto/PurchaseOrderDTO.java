package com.boutique.pos.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class PurchaseOrderDTO {
    private Long id;

    private String orderNumber;

    private Long supplierId;

    private String supplierName;

    private String status;

    private BigDecimal total;

    private LocalDateTime createdAt;

    private LocalDateTime receivedAt;

    private List<PurchaseItemDTO> items;
}
