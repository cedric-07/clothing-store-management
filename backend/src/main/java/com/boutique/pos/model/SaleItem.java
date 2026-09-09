package com.boutique.pos.model;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "sale_items")
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sale_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sale_item_sale"))
    private Sale sale;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_variant_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sale_item_product_variant"))
    private ProductVariant productVariant;
    @Column(nullable = false)
    private Integer quantity;
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal unitPrice;
    @Column(name = "discount", nullable = false, precision = 10, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;
    @Column(name = "sub_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
}
