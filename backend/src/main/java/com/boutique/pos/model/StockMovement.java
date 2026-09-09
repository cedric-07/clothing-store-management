package com.boutique.pos.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stock_movements")
public class StockMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_variant_id", nullable = false, foreignKey = @ForeignKey(name = "fk_stock_movement_product_variant"))
    private ProductVariant productVariant;
    @Column(nullable = false)
    private Integer quantity;
    @Column(name = "movement_type", nullable = false, length = 50)
    private String movementType; // e.g., "IN" for stock in, "OUT" for stock out
    @Column(name = "stock_before")
    private Integer stockBefore;; // Optional reason for the stock movement
    @Column(name = "stock_after")
    private Integer stockAfter;
    @Column(name = "reference", length = 100)
    private String reference;
    private String description; // Optional reason for the stock movement
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_stock_movement_user"))
    private User createdBy; // Optional reference to the user who created the stock movement
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
