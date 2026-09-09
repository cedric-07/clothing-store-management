package com.boutique.pos.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.boutique.pos.model.Payment;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
    List<Payment> findBySaleId(Long saleId);
}
