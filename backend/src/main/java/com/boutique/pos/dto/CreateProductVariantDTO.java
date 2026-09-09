package com.boutique.pos.dto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductVariantDTO {
    private Long id;
    private String sku;
    private Long productId;
    private String barcode;
    private String size;
    private String color;
    private Integer quantity;
    private Integer minimumStock;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
