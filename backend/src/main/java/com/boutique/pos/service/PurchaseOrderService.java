package com.boutique.pos.service;

import com.boutique.pos.dto.*;
import com.boutique.pos.model.*;
import com.boutique.pos.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PurchaseOrderService {
    private final SupplierRepository supplierRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository productVariantRepository;
    private final StockMovementRepository stockMovementRepository;

    public PurchaseOrderService(SupplierRepository supplierRepository, PurchaseOrderRepository purchaseOrderRepository, PurchaseItemRepository purchaseItemRepository, UserRepository userRepository, ProductVariantRepository productVariantRepository, StockMovementRepository stockMovementRepository) {
        this.supplierRepository = supplierRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.userRepository = userRepository;
        this.productVariantRepository = productVariantRepository;
        this.stockMovementRepository = stockMovementRepository;
    }


    @Transactional
    public PurchaseOrderDTO create(CreatePurchaseOrderDTO dto) {
        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        User user = userRepository.findById(dto.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("User not found"));
        PurchaseOrder order = new PurchaseOrder();
        order.setSupplier(supplier);
        order.setCreatedBy(user);
        order.setStatus("DRAFT");
        order.setTotalAmount(BigDecimal.ZERO);
        order.setOrderNumber(generateOrderNumber());
        purchaseOrderRepository.save(order);

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (PurchaseItemRequestDTO itemDTO : dto.getItems()) {
            ProductVariant variant = productVariantRepository.findById(itemDTO.getProductVariantId())
                    .orElseThrow(() -> new RuntimeException("Product variant not found"));
            BigDecimal itemTotal = itemDTO.getPurchasePrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            PurchaseItem item = new PurchaseItem();
            item.setPurchaseOrder(order);
            item.setProductVariant(variant);
            item.setQuantityOrdered(itemDTO.getQuantity());
            item.setQuantityReceived(0);
            item.setPurchasePrice(itemDTO.getPurchasePrice());
            purchaseItemRepository.save(item);
            totalAmount = totalAmount.add(itemTotal);
        }
        order.setTotalAmount(totalAmount);
        var orderItem = purchaseOrderRepository.save(order);
        return toDTO(orderItem);

    }

    @Transactional
    public PurchaseOrderDTO receive(
            Long id,
            ReceivePurchaseOrderDTO dto
    ) {

        PurchaseOrder order =
                purchaseOrderRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Purchase order not found"
                                ));

        User user = userRepository
                .findById(dto.getUserId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        for (ReceivePurchaseItemDTO receiveDTO :
                dto.getItems()) {

            PurchaseItem item =
                    purchaseItemRepository
                            .findById(
                                    receiveDTO.getPurchaseItemId()
                            )
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Purchase item not found"
                                    ));

            if (!item.getPurchaseOrder()
                    .getId()
                    .equals(id)) {

                throw new RuntimeException(
                        "Purchase item does not belong to this order"
                );
            }

            int quantityToReceive =
                    receiveDTO.getQuantityReceived();

            if (quantityToReceive <= 0) {
                throw new RuntimeException(
                        "Quantity must be greater than 0"
                );
            }

            int totalReceived =
                    item.getQuantityReceived()
                            + quantityToReceive;

            if (totalReceived >
                    item.getQuantityOrdered()) {

                throw new RuntimeException(
                        "Received quantity exceeds ordered quantity"
                );
            }

            ProductVariant variant =
                    item.getProductVariant();

            int stockBefore =
                    variant.getQuantity();

            int stockAfter =
                    stockBefore + quantityToReceive;

            variant.setQuantity(stockAfter);

            productVariantRepository.save(variant);

            item.setQuantityReceived(totalReceived);

            purchaseItemRepository.save(item);

            StockMovement movement =
                    StockMovement.builder()
                            .productVariant(variant)
                            .movementType("PURCHASE")
                            .quantity(quantityToReceive)
                            .stockBefore(stockBefore)
                            .stockAfter(stockAfter)
                            .reference(
                                    order.getOrderNumber()
                            )
                            .description(
                                    "Purchase order reception"
                            )
                            .createdBy(user)
                            .build();

            stockMovementRepository.save(movement);
        }

        List<PurchaseItem> allItems =
                purchaseItemRepository
                        .findByPurchaseOrderId(id);

        boolean allReceived =
                allItems.stream()
                        .allMatch(item ->
                                item.getQuantityReceived()
                                        .equals(
                                                item.getQuantityOrdered()
                                        )
                        );

        if (allReceived) {

            order.setStatus("RECEIVED");
            order.setReceivedAt(
                    LocalDateTime.now()
            );

        } else {

            order.setStatus(
                    "PARTIALLY_RECEIVED"
            );
        }

        return toDTO(
                purchaseOrderRepository.save(order)
        );
    }

    private PurchaseOrderDTO toDTO(
            PurchaseOrder order
    ) {

        List<PurchaseItemDTO> items =
                purchaseItemRepository
                        .findByPurchaseOrderId(
                                order.getId()
                        )
                        .stream()
                        .map(item ->
                                PurchaseItemDTO.builder()
                                        .id(item.getId())
                                        .productVariantId(
                                                item.getProductVariant()
                                                        .getId()
                                        )
                                        .sku(
                                                item.getProductVariant()
                                                        .getSku()
                                        )
                                        .quantityOrdered(
                                                item.getQuantityOrdered()
                                        )
                                        .quantityReceived(
                                                item.getQuantityReceived()
                                        )
                                        .purchasePrice(
                                                item.getPurchasePrice()
                                        )
                                        .build()
                        )
                        .toList();

        return PurchaseOrderDTO.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .supplierId(
                        order.getSupplier().getId()
                )
                .supplierName(
                        order.getSupplier().getName()
                )
                .status(order.getStatus())
                .total(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .receivedAt(order.getReceivedAt())
                .items(items)
                .build();
    }

    private String generateOrderNumber() {

        return "PO-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    public PurchaseOrderDTO getById(Long id) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase order not found"));
        return toDTO(order);
    }
    public List<PurchaseOrderDTO> getAll() {
        List<PurchaseOrder> orders = purchaseOrderRepository.findAll();
        return orders.stream().map(this::toDTO).toList();
    }
    public PurchaseOrderDTO submit(Long id) {
        PurchaseOrder order = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase order not found"));
        order.setStatus("SUBMITTED");
        purchaseOrderRepository.save(order);
        return toDTO(order);
    }
}
