package com.boutique.pos.controller;

import com.boutique.pos.dto.CreateSupplierDTO;
import com.boutique.pos.dto.SupplierDTO;
import com.boutique.pos.service.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "http://localhost:5173")
public class SupplierController {
    private final SupplierService supplierService;
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }
    @PostMapping
    public ResponseEntity<?> createSupplier(@RequestBody CreateSupplierDTO createSupplierDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(supplierService.create(createSupplierDTO));
    }
    @GetMapping
    public ResponseEntity<List<SupplierDTO>> getAll() {
        return ResponseEntity.ok(supplierService.getAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getById(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> update(@PathVariable Long id, @RequestBody CreateSupplierDTO createSupplierDTO) {
        return ResponseEntity.ok(supplierService.update(id, createSupplierDTO));
    }
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<SupplierDTO> deactivate(@PathVariable Long id) {
        supplierService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
