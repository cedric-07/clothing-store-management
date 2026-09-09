package com.boutique.pos.controller;

import com.boutique.pos.model.Product;
import com.boutique.pos.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PostMapping("/create")
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product createdProduct = productService.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }
    @PostMapping("/createall")
    public List<Product> createProducts(@RequestBody List<Product> products) {
        return productService.createProducts(products);
    }
    @GetMapping
    public ResponseEntity<List<Product>> getAll() {
        List<Product> products = productService.getAll();
        return ResponseEntity.ok(products);
    }
    @GetMapping("/active")
    public ResponseEntity<List<Product>> getActiveProducts() {
        List<Product> activeProducts = productService.getActiveProducts();
        return ResponseEntity.ok(activeProducts);
    }
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        List<Product> products = productService.searchProducts(name);
        return ResponseEntity.ok(products);
    }
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
    @GetMapping("/brand/{brandId}")
    public ResponseEntity<List<Product>> getByBrand(@PathVariable Long brandId) {
        List<Product> products = productService.getByBrand(brandId);
        return ResponseEntity.ok(products);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.update(id, product);
        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
       productService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        productService.activate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getById(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
