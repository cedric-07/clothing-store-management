package com.boutique.pos.repository;

import com.boutique.pos.model.InventoryItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryItemRepository extends org.springframework.data.jpa.repository.JpaRepository<InventoryItem, Long>{
    List<InventoryItem> findByInventoryId(Long productVariantId);
    Optional<InventoryItem> findByInventoryIdAndProductVariantId(Long inventoryId, Long productVariantId);
}
