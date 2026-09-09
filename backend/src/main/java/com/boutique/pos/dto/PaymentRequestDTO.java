package com.boutique.pos.dto;
import lombok.*;
@Getter
@Setter
public class PaymentRequestDTO {
    private String paymentMethod;
    private java.math.BigDecimal amount;
    private String transactionReference;
}
