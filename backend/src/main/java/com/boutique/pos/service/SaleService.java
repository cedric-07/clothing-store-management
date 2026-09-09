package com.boutique.pos.service;

import com.boutique.pos.dto.CreateSaleDTO;
import com.boutique.pos.dto.SaleItemRequestDTO;
import com.boutique.pos.model.*;
import com.boutique.pos.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class SaleService {
    private final SaleItemRepository saleItemRepository;
    private final PaymentRepository paymentRepository;
    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository productVariantRepository;
    private final StockMovementRepository stockRepository;

    public SaleService(SaleItemRepository saleItemRepository, PaymentRepository paymentRepository, SaleRepository saleRepository, CustomerRepository customerRepository, UserRepository userRepository, ProductVariantRepository productVariantRepository, StockMovementRepository stockRepository) {
        this.saleItemRepository = saleItemRepository;
        this.paymentRepository = paymentRepository;
        this.saleRepository = saleRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.productVariantRepository = productVariantRepository;
        this.stockRepository = stockRepository;
    }
    private String generateSaleNumber() {
        return "SALE-000" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    @Transactional
    public Sale create(CreateSaleDTO createSaleDTO) {

        User cashier = userRepository.findById(createSaleDTO.getCashierId())
                .orElseThrow(() -> new RuntimeException("Cashier not found"));

        Customer customer = null;

        if (createSaleDTO.getCustomerId() != null) {
            customer = customerRepository
                    .findById(createSaleDTO.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
        }

        /*
         * 1. Calcul du subtotal AVANT de sauvegarder la vente
         */
        BigDecimal subtotal = BigDecimal.ZERO;

        for (SaleItemRequestDTO itemDTO : createSaleDTO.getItems()) {

            ProductVariant productVariant =
                    productVariantRepository
                            .findById(itemDTO.getProductVariantId())
                            .orElseThrow(() ->
                                    new RuntimeException("Product variant not found")
                            );

            if (productVariant.getQuantity() < itemDTO.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for product variant: "
                                + productVariant.getId()
                );
            }

            BigDecimal unitPrice =
                    productVariant.getProduct().getSellingPrice();

            BigDecimal itemDiscount =
                    itemDTO.getDiscount() != null
                            ? itemDTO.getDiscount()
                            : BigDecimal.ZERO;

            BigDecimal lineSubtotal =
                    unitPrice
                            .multiply(
                                    BigDecimal.valueOf(itemDTO.getQuantity())
                            )
                            .subtract(itemDiscount);

            subtotal = subtotal.add(lineSubtotal);
        }

        /*
         * 2. Réduction globale
         */
        BigDecimal discount =
                createSaleDTO.getDiscount() != null
                        ? createSaleDTO.getDiscount()
                        : BigDecimal.ZERO;

        BigDecimal total =
                subtotal.subtract(discount);

        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException(
                    "Total amount cannot be negative"
            );
        }

        /*
         * 3. Amount received
         */
        BigDecimal amountReceived =
                createSaleDTO.getAmountReceived() != null
                        ? createSaleDTO.getAmountReceived()
                        : BigDecimal.ZERO;

        if (amountReceived.compareTo(total) < 0) {
            throw new RuntimeException(
                    "Insufficient amount received"
            );
        }

        /*
         * 4. Maintenant la vente possède déjà subtotal + total
         */
        Sale sale = new Sale();

        sale.setSaleNumber(generateSaleNumber());
        sale.setCashier(cashier);
        sale.setCustomer(customer);
        sale.setStatus("COMPLETED");

        sale.setSubtotal(subtotal);
        sale.setDiscount(discount);
        sale.setTotal(total);

        sale.setAmountReceived(amountReceived);
        sale.setChangeAmount(BigDecimal.ZERO);

        /*
         * Maintenant total n'est plus NULL.
         */
        sale = saleRepository.save(sale);

        /*
         * 5. Création des SaleItems
         *    + diminution du stock
         *    + StockMovement
         */
        for (SaleItemRequestDTO itemDTO : createSaleDTO.getItems()) {

            ProductVariant productVariant =
                    productVariantRepository
                            .findById(itemDTO.getProductVariantId())
                            .orElseThrow(() ->
                                    new RuntimeException("Product variant not found")
                            );

            BigDecimal unitPrice =
                    productVariant.getProduct().getSellingPrice();

            BigDecimal itemDiscount =
                    itemDTO.getDiscount() != null
                            ? itemDTO.getDiscount()
                            : BigDecimal.ZERO;

            BigDecimal lineSubtotal =
                    unitPrice
                            .multiply(
                                    BigDecimal.valueOf(itemDTO.getQuantity())
                            )
                            .subtract(itemDiscount);

            /*
             * Sale Item
             */
            SaleItem saleItem = new SaleItem();

            saleItem.setSale(sale);
            saleItem.setProductVariant(productVariant);
            saleItem.setQuantity(itemDTO.getQuantity());
            saleItem.setUnitPrice(unitPrice);
            saleItem.setDiscount(itemDiscount);
            saleItem.setSubtotal(lineSubtotal);

            saleItemRepository.save(saleItem);

            /*
             * Mise à jour stock
             */
            int stockBefore =
                    productVariant.getQuantity();

            int stockAfter =
                    stockBefore - itemDTO.getQuantity();

            productVariant.setQuantity(stockAfter);

            productVariantRepository.save(productVariant);

            /*
             * Historique stock
             */
            StockMovement stockMovement =
                    StockMovement.builder()
                            .productVariant(productVariant)
                            .quantity(-itemDTO.getQuantity())
                            .movementType("SALE")
                            .stockBefore(stockBefore)
                            .stockAfter(stockAfter)
                            .reference(sale.getSaleNumber())
                            .description(
                                    "Sale of product variant: "
                                            + productVariant.getId()
                            )
                            .createdBy(cashier)
                            .build();

            stockRepository.save(stockMovement);
        }

        /*
         * 6. Paiements
         */
        BigDecimal totalPaid =
                BigDecimal.ZERO;

        if (createSaleDTO.getPayments() != null) {

            for (var paymentDTO : createSaleDTO.getPayments()) {

                if (paymentDTO.getAmount() == null
                        || paymentDTO.getAmount()
                        .compareTo(BigDecimal.ZERO) <= 0) {

                    throw new RuntimeException(
                            "Payment amount must be greater than 0"
                    );
                }

                Payment payment = new Payment();

                payment.setSale(sale);
                payment.setPaymentMethod(
                        paymentDTO.getPaymentMethod()
                );

                payment.setAmount(
                        paymentDTO.getAmount()
                );

                payment.setTransactionReference(
                        paymentDTO.getTransactionReference()
                );

                paymentRepository.save(payment);

                totalPaid =
                        totalPaid.add(
                                paymentDTO.getAmount()
                        );
            }
        }

        /*
         * 7. Vérification paiement
         */
        if (totalPaid.compareTo(total) < 0) {
            throw new RuntimeException(
                    "Insufficient payment amount"
            );
        }

        /*
         * 8. Monnaie à rendre
         */
        BigDecimal changeAmount =
                totalPaid.subtract(total);

        sale.setChangeAmount(changeAmount);

        /*
         * 9. Points fidélité
         *
         * Ici :
         * 1000 Ar = 1 point
         */
        if (customer != null) {

            int pointsEarned =
                    total.divideToIntegralValue(
                            BigDecimal.valueOf(1000)
                    ).intValue();

            customer.setLoyaltyPoints(
                    customer.getLoyaltyPoints()
                            + pointsEarned
            );

            customerRepository.save(customer);
        }

        return saleRepository.save(sale);
    }

    public List<Sale> getAll() {
        return saleRepository.findAll();
    }
    public Sale getById(Long id) {
        return saleRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found"));
    }

    @Transactional
    public void returnItem(Long saleId, Long saleItemId, Integer quantity, Long userId) {
        Sale sale = saleRepository.findById(saleId).orElseThrow(() -> new RuntimeException("Sale not found"));
        SaleItem saleItem = saleItemRepository.findById(saleItemId).orElseThrow(() -> new RuntimeException("Sale item not found"));
        if (!saleItem.getSale().getId().equals(saleId)) {
            throw new RuntimeException("Sale item does not belong to the sale");
        }
        if (quantity <= 0 || quantity > saleItem.getQuantity()) {
            throw new RuntimeException("Invalid return quantity");
        }
        saleItem.setQuantity(saleItem.getQuantity() - quantity);
        saleItemRepository.save(saleItem);
        StockMovement stockMovement = StockMovement.builder()
                .productVariant(saleItem.getProductVariant())
                .quantity(quantity)
                .movementType("RETURN")
                .stockBefore(saleItem.getProductVariant().getQuantity())
                .stockAfter(saleItem.getProductVariant().getQuantity() + quantity)
                .reference(generateReturnReference())
                .description("Custom return of : " + saleItem.getProductVariant().getId())
                .createdBy(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")))
                .build();
        stockRepository.save(stockMovement);
    }
    private String generateReturnReference() {
        return "RETURN-000" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}
