package com.boutique.pos.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sale_id", nullable = false, foreignKey = @ForeignKey(name = "fk_payment_sale"))
    private Sale sale;
    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;
    @Column(nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal amount;
    @Column(name = "transaction_reference", length = 100)
    private String transactionReference;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
