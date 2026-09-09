package com.boutique.pos.dto;
import com.boutique.pos.model.Brand;
import com.boutique.pos.model.Category;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Category category;
    private Brand brand;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
