package com.boutique.pos.controller;

import com.boutique.pos.model.Brand;
import com.boutique.pos.model.Category;
import com.boutique.pos.service.BrandService;
import com.boutique.pos.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/brands")
@CrossOrigin(origins = "http://localhost:5173")
public class BrandController {
    private final BrandService brandService;
    private final CategoryService categoryService;

    public BrandController(BrandService brandService, CategoryService categoryService) {
        this.brandService = brandService;
        this.categoryService = categoryService;
    }
    @PostMapping("/create")
    public ResponseEntity<Brand> create(@RequestBody Brand brand) {
        Brand createdBrand = brandService.create(brand);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBrand);
    }

    @PostMapping("/category/create")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

}
