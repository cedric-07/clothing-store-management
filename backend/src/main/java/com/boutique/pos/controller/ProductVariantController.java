package com.boutique.pos.controller;

import com.boutique.pos.model.ProductVariant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.boutique.pos.service.ProductVariantService;

import java.util.List;

@RestController
@RequestMapping("/api/product-variants")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductVariantController {
    private final ProductVariantService productVariantService;

    public ProductVariantController(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    @PostMapping("/product/{productId}/create")
    public ResponseEntity<ProductVariant> Create(@PathVariable Long productId, @RequestBody ProductVariant productVariant) {
        ProductVariant createdProductVariant = productVariantService.create(productVariant, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProductVariant);
    }
    @GetMapping
    public ResponseEntity<List<ProductVariant>> getAll() {
        List<ProductVariant> productVariants = productVariantService.getAll();
        return ResponseEntity.ok(productVariants);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductVariant> getById(@PathVariable Long id) {
        ProductVariant productVariant = productVariantService.getById(id);
        if (productVariant != null) {
            return ResponseEntity.ok(productVariant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductVariant>> getByProductId(@PathVariable Long productId) {
        List<ProductVariant> productVariants = productVariantService.getByProduct(productId);
        return ResponseEntity.ok(productVariants);
    }
    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<ProductVariant> getByBarcode(@PathVariable String barcode) {
        ProductVariant productVariant = productVariantService.getByBarcode(barcode);
        if (productVariant != null) {
            return ResponseEntity.ok(productVariant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/sku/{sku}")
    public ResponseEntity<ProductVariant> getBySku(@PathVariable String sku) {
        ProductVariant productVariant = productVariantService.getBySku(sku);
        if (productVariant != null) {
            return ResponseEntity.ok(productVariant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductVariant>> getLowStock() {
        List<ProductVariant> lowStockVariants = productVariantService.getLowStock();
        return ResponseEntity.ok(lowStockVariants);
    }
    @PatchMapping("/{id}/update-stock")
    public ResponseEntity<ProductVariant> updateStock(@PathVariable Long id, @RequestParam Integer quantity) {
        try {
            ProductVariant updatedProductVariant = productVariantService.updteStock(id, quantity);
            return ResponseEntity.ok(updatedProductVariant);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PatchMapping("/{id}/add-stock")
    public ResponseEntity<ProductVariant> addStock(@PathVariable Long id, @RequestParam Integer quantity) {
        try {
            ProductVariant updatedProductVariant = productVariantService.addStock(id, quantity);
            return ResponseEntity.ok(updatedProductVariant);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PatchMapping("/{id}/remove-stock")
    public ResponseEntity<ProductVariant> removeStock(@PathVariable Long id, @RequestParam Integer quantity) {
        try {
            ProductVariant updatedProductVariant = productVariantService.removeStock(id, quantity);
            return ResponseEntity.ok(updatedProductVariant);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ProductVariant> deactivate(@PathVariable Long id) {
        ProductVariant deactivatedProductVariant = productVariantService.deactivate(id);
        if (deactivatedProductVariant != null) {
            return ResponseEntity.ok(deactivatedProductVariant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ProductVariant> activate(@PathVariable Long id) {
        ProductVariant activatedProductVariant = productVariantService.activate(id);
        if (activatedProductVariant != null) {
            return ResponseEntity.ok(activatedProductVariant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
