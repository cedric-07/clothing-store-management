package com.boutique.pos.model;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "sale_number", nullable = false, unique = true)
    private String saleNumber;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_sale_customer"))
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cashier_id", nullable = false)
    private User cashier;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    @Column(name = "amount_received", precision = 10, scale = 2)
    private BigDecimal amountReceived;
    @Column(name = "change_amount", precision = 10, scale = 2)
    private BigDecimal changeAmount = BigDecimal.ZERO;
    @Column(name = "status", nullable = false)
    private String status = "COMPLETED";
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<SaleItem> items = new java.util.ArrayList<>();
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<Payment> payments = new java.util.ArrayList<>();

}
