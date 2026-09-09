package com.boutique.pos.dto;
import lombok.*;
import java.math.BigDecimal;
import com.boutique.pos.model.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductDTO {
    private String name;
    private String description;
    private Long categoryId;
    private Long brandId;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
}
