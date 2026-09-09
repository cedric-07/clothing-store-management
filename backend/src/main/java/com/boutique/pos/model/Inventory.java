package com.boutique.pos.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "created_by",
            foreignKey = @ForeignKey(
                    name = "fk_inventory_user"
            )
    )
    private User createdBy;

    @Column(
            name = "inventory_number",
            nullable = false,
            unique = true,
            length = 50
    )
    private String inventoryNumber;

    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    @Builder.Default
    private String status = "IN_PROGRESS";

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;
}