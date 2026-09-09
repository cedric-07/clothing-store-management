package com.boutique.pos.dto;
import lombok.Getter;
import lombok.Setter;

import java.math.*;
import java.util.List;

@Getter
@Setter
public class CreateSaleDTO {
    private Long customerId;
    private Long cashierId;
    private BigDecimal amountReceived;
    private BigDecimal discount;
    private List<SaleItemRequestDTO> items;
    private List<PaymentRequestDTO> payments;
}
