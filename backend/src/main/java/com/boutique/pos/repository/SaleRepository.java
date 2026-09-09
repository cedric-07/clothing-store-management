package com.boutique.pos.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.boutique.pos.model.Sale;

import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long>{
    Optional<Sale> findBySaleNumber(String saleNumber);
}
