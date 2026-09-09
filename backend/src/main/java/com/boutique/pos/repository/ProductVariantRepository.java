package com.boutique.pos.repository;
import com.boutique.pos.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long>{
    Optional<ProductVariant> findBySku(String sku);
    Optional<ProductVariant> findByBarcode(String barcode);
    boolean existsBySku(String sku);
    boolean existsByBarcode(String barcode);
    List<ProductVariant> findByProductId(Long productId);

    List<ProductVariant> findByActiveTrue();
    List<ProductVariant> findByQuantityLessThanEqual(Integer quantity);
    @Query("SELECT pv FROM ProductVariant pv WHERE pv.quantity <= pv.minimumStock AND pv.active = true")
    List<ProductVariant> findLowStockVariants();
}
