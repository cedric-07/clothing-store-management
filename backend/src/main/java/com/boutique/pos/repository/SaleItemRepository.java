package com.boutique.pos.repository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.boutique.pos.model.SaleItem;

import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long>{
    List<SaleItem> findBySaleId(Long saleId);
}
