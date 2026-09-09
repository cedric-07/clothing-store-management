package com.boutique.pos.repository;
import com.boutique.pos.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByBrandId(Long brandId);
    List<Product> findByActiveTrue(Boolean active);
    List<Product> findByNameContainingIgnoreCase(String name);

}
