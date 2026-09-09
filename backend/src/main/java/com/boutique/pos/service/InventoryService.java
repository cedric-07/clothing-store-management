package com.boutique.pos.service;

import com.boutique.pos.dto.*;
import com.boutique.pos.model.*;
import com.boutique.pos.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final ProductVariantRepository variantRepository;
    private final UserRepository userRepository;
    private final StockMovementRepository stockMovementRepository;

    public InventoryService(
            InventoryRepository inventoryRepository,
            InventoryItemRepository inventoryItemRepository,
            ProductVariantRepository variantRepository,
            UserRepository userRepository,
            StockMovementRepository stockMovementRepository
    ) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.variantRepository = variantRepository;
        this.userRepository = userRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    // =====================================================
    // CREATE INVENTORY
    // =====================================================

    @Transactional
    public InventoryDTO create(CreateInventoryDTO dto) {

        if (dto == null) {
            throw new RuntimeException(
                    "Request body cannot be null"
            );
        }

        if (dto.getCreatedBy() == null) {
            throw new RuntimeException(
                    "createdBy is required"
            );
        }

        User user = userRepository
                .findById(dto.getCreatedBy())
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found with id: "
                                        + dto.getCreatedBy()
                        )
                );

        Inventory inventory = Inventory.builder()
                .inventoryNumber(generateInventoryNumber())
                .status("IN_PROGRESS")
                .createdBy(user)
                .build();

        // Très important :
        // on sauvegarde d'abord l'inventaire pour obtenir son ID
        inventory = inventoryRepository.save(inventory);

        List<ProductVariant> variants =
                variantRepository.findByActiveTrue();

        for (ProductVariant variant : variants) {

            InventoryItem item =
                    InventoryItem.builder()
                            .inventory(inventory)
                            .productVariant(variant)
                            .expectedQuantity(
                                    variant.getQuantity()
                            )
                            .quantityCounted(0)

                            // IMPORTANT :
                            // pas -variant.getQuantity()
                            .difference(0)

                            .build();

            inventoryItemRepository.save(item);
        }

        return toDTO(inventory);
    }

    // =====================================================
    // COUNT ONE PRODUCT
    // =====================================================

    @Transactional
    public InventoryDTO countItem(
            Long inventoryId,
            CountInventoryItemDTO dto
    ) {

        if (inventoryId == null) {
            throw new RuntimeException(
                    "Inventory id is required"
            );
        }

        if (dto == null) {
            throw new RuntimeException(
                    "Request body cannot be null"
            );
        }

        if (dto.getProductVariantId() == null) {
            throw new RuntimeException(
                    "productVariantId is required"
            );
        }

        if (dto.getCountedQuantity() == null) {
            throw new RuntimeException(
                    "countedQuantity is required"
            );
        }

        if (dto.getCountedQuantity() < 0) {
            throw new RuntimeException(
                    "countedQuantity cannot be negative"
            );
        }

        Inventory inventory =
                getInventory(inventoryId);

        if (!"IN_PROGRESS".equals(
                inventory.getStatus()
        )) {
            throw new RuntimeException(
                    "Inventory is already validated"
            );
        }

        InventoryItem item =
                inventoryItemRepository
                        .findByInventoryIdAndProductVariantId(
                                inventoryId,
                                dto.getProductVariantId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product variant "
                                                + dto.getProductVariantId()
                                                + " is not part of inventory "
                                                + inventoryId
                                )
                        );

        item.setQuantityCounted(
                dto.getCountedQuantity()
        );

        int difference =
                dto.getCountedQuantity()
                        - item.getExpectedQuantity();

        item.setDifference(difference);

        inventoryItemRepository.save(item);

        return toDTO(inventory);
    }

    // =====================================================
    // VALIDATE INVENTORY
    // =====================================================

    @Transactional
    public InventoryDTO validate(
            Long inventoryId
    ) {

        if (inventoryId == null) {
            throw new RuntimeException(
                    "Inventory id is required"
            );
        }

        Inventory inventory =
                getInventory(inventoryId);

        if (!"IN_PROGRESS".equals(
                inventory.getStatus()
        )) {
            throw new RuntimeException(
                    "Inventory already validated"
            );
        }

        List<InventoryItem> items =
                inventoryItemRepository
                        .findByInventoryId(
                                inventoryId
                        );

        for (InventoryItem item : items) {

            if (item.getDifference() == 0) {
                continue;
            }

            ProductVariant variant =
                    item.getProductVariant();

            int stockBefore =
                    variant.getQuantity();

            int stockAfter =
                    item.getQuantityCounted();

            int movementQuantity =
                    stockAfter - stockBefore;

            variant.setQuantity(stockAfter);

            variantRepository.save(variant);

            StockMovement movement =
                    StockMovement.builder()
                            .productVariant(variant)
                            .movementType(
                                    "INVENTORY_ADJUSTMENT"
                            )
                            .quantity(
                                    movementQuantity
                            )
                            .stockBefore(
                                    stockBefore
                            )
                            .stockAfter(
                                    stockAfter
                            )
                            .reference(
                                    inventory.getInventoryNumber()
                            )
                            .description(
                                    "Physical inventory adjustment"
                            )
                            .createdBy(
                                    inventory.getCreatedBy()
                            )
                            .build();

            stockMovementRepository.save(
                    movement
            );
        }

        inventory.setStatus("VALIDATED");

        inventory.setValidatedAt(
                LocalDateTime.now()
        );

        inventoryRepository.save(inventory);

        return toDTO(inventory);
    }

    // =====================================================
    // GET ALL
    // =====================================================

    public List<InventoryDTO> getAll() {

        return inventoryRepository
                .findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    // =====================================================
    // GET BY ID
    // =====================================================

    public InventoryDTO getById(Long id) {

        if (id == null) {
            throw new RuntimeException(
                    "Inventory id is required"
            );
        }

        return toDTO(
                getInventory(id)
        );
    }

    // =====================================================
    // FIND INVENTORY
    // =====================================================

    private Inventory getInventory(Long id) {

        if (id == null) {
            throw new RuntimeException(
                    "Inventory id cannot be null"
            );
        }

        return inventoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Inventory not found with id: "
                                        + id
                        )
                );
    }

    // =====================================================
    // ENTITY -> DTO
    // =====================================================

    private InventoryDTO toDTO(
            Inventory inventory
    ) {

        List<InventoryItemDTO> items =
                inventoryItemRepository
                        .findByInventoryId(
                                inventory.getId()
                        )
                        .stream()
                        .map(item ->
                                InventoryItemDTO.builder()
                                        .id(
                                                item.getId()
                                        )
                                        .productVariantId(
                                                item.getProductVariant()
                                                        .getId()
                                        )
                                        .sku(
                                                item.getProductVariant()
                                                        .getSku()
                                        )
                                        .expectedQuantity(
                                                item.getExpectedQuantity()
                                        )
                                        .countedQuantity(
                                                item.getQuantityCounted()
                                        )
                                        .difference(
                                                item.getDifference()
                                        )
                                        .build()
                        )
                        .toList();

        return InventoryDTO.builder()
                .id(
                        inventory.getId()
                )
                .inventoryNumber(
                        inventory.getInventoryNumber()
                )
                .status(
                        inventory.getStatus()
                )
                .createdAt(
                        inventory.getCreatedAt()
                )
                .validatedAt(
                        inventory.getValidatedAt()
                )
                .items(items)
                .build();
    }

    // =====================================================
    // NUMBER GENERATION
    // =====================================================

    private String generateInventoryNumber() {

        return "INV-"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}