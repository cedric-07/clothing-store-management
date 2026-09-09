package com.boutique.pos.repository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.boutique.pos.model.StockMovement;

import java.util.List;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    List<StockMovement> findByProductVariantIdOrderByCreatedAtDesc(Long productVariantId);
}
