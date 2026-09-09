package com.boutique.pos.controller;
import com.boutique.pos.dto.CreateSaleDTO;
import com.boutique.pos.dto.ReturnItemDTO;
import com.boutique.pos.model.Sale;
import com.boutique.pos.service.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
@CrossOrigin(origins = "http://localhost:5173")
public class SaleController {
    private final SaleService saleService;
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }
    @PostMapping
    public ResponseEntity<Sale> create(@RequestBody CreateSaleDTO createSaleDTO) {
        Sale createdSale = saleService.create(createSaleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
    }
    @GetMapping
    public ResponseEntity<List<Sale>> getAll() {
        List<Sale> sales = saleService.getAll();
        return ResponseEntity.ok(sales);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Sale> getById(@PathVariable Long id) {
        Sale sale = saleService.getById(id);
        if (sale != null) {
            return ResponseEntity.ok(sale);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PatchMapping("/{saleId}/return")
    public ResponseEntity<Void> returnItem(
            @PathVariable Long saleId,
            @RequestBody ReturnItemDTO dto
    ) {

        saleService.returnItem(
                saleId,
                dto.getSaleItemId(),
                dto.getQuantity(),
                dto.getUserId()
        );

        return ResponseEntity.noContent().build();
    }
}
