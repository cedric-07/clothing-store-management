package com.boutique.pos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "inventory_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_inventory_variant",
                        columnNames = {
                                "inventory_id",
                                "product_variant_id"
                        }
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "inventory_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_inventory_item_inventory"
            )
    )
    private Inventory inventory;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "product_variant_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_inventory_item_variant"
            )
    )
    private ProductVariant productVariant;

    @Column(
            name = "expected_quantity",
            nullable = false
    )
    private Integer expectedQuantity;

    @Column(
            name = "quantity_counted",
            nullable = false
    )
    @Builder.Default
    private Integer quantityCounted = 0;

    @Column(
            name = "difference",
            nullable = false
    )
    @Builder.Default
    private Integer difference = 0;
}