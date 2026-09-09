package com.boutique.pos.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // =========================
    // CATEGORY
    // =========================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "category_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_category")
    )
    private Category category;

    // =========================
    // BRAND
    // =========================

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "brand_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_brand")
    )
    private Brand brand;

    // =========================
    // PRICES
    // =========================

    @Column(
            name = "purchase_price",
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal purchasePrice;

    @Column(
            name = "selling_price",
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal sellingPrice;

    // =========================
    // STATUS
    // =========================

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    // =========================
    // DATES
    // =========================

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    // =========================
    // VARIANTS
    // =========================

    @Builder.Default
    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProductVariant> variants = new ArrayList<>();


    // =========================
    // LIFECYCLE
    // =========================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }

        if (purchasePrice == null) {
            purchasePrice = BigDecimal.ZERO;
        }

        if (sellingPrice == null) {
            sellingPrice = BigDecimal.ZERO;
        }

        if (active == null) {
            active = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


    // =========================
    // VARIANT HELPERS
    // =========================

    public void addVariant(ProductVariant variant) {

        variants.add(variant);
        variant.setProduct(this);
    }

    public void removeVariant(ProductVariant variant) {

        variants.remove(variant);
        variant.setProduct(null);
    }
}