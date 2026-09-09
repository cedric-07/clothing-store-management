package com.boutique.pos.service;
import com.boutique.pos.model.Product;
import com.boutique.pos.model.ProductVariant;
import com.boutique.pos.repository.ProductVariantRepository;
import com.boutique.pos.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVariantService {
    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;

    public ProductVariantService(ProductVariantRepository productVariantRepository, ProductRepository productRepository) {
        this.productVariantRepository = productVariantRepository;
        this.productRepository = productRepository;
    }
    public ProductVariant create(ProductVariant productVariant, Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Product not found with id: " + productId
                        )
                );

        if (productVariantRepository.existsBySku(productVariant.getSku())) {
            throw new IllegalArgumentException("SKU already exists");
        }

        if (productVariantRepository.existsByBarcode(productVariant.getBarcode())) {
            throw new IllegalArgumentException("Barcode already exists");
        }

        productVariant.setProduct(product);

        return productVariantRepository.save(productVariant);
    }

    public List<ProductVariant> getAll() {
        return productVariantRepository.findAll();
    }

    public ProductVariant getById(Long id) {
        return productVariantRepository.findById(id).orElse(null);
    }
    public ProductVariant getBySku(String sku) {
        return productVariantRepository.findBySku(sku).orElse(null);
    }
    public List<ProductVariant> getByProduct(Long productId) {
        return productVariantRepository.findByProductId(productId);
    }
    public ProductVariant getByBarcode(String barcode) {
        return productVariantRepository.findByBarcode(barcode).orElse(null);
    }
    public List<ProductVariant> getLowStock(){
        return productVariantRepository.findLowStockVariants();
    }

    public ProductVariant updteStock(Long varianId, Integer quantity) {
        if(quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        ProductVariant productVariant = productVariantRepository.findById(varianId).orElse(null);
        if(productVariant == null) {
            throw new IllegalArgumentException("Product variant not found");
        }
        productVariant.setQuantity(quantity);
        return productVariantRepository.save(productVariant);
    }

    public ProductVariant addStock(Long varianId, Integer quantity) {
        if(quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        ProductVariant productVariant = productVariantRepository.findById(varianId).orElse(null);
        if(productVariant == null) {
            throw new IllegalArgumentException("Product variant not found");
        }
        productVariant.setQuantity(productVariant.getQuantity() + quantity);
        return productVariantRepository.save(productVariant);
    }
    public ProductVariant removeStock(Long varianId, Integer quantity) {
        if(quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        ProductVariant productVariant = productVariantRepository.findById(varianId).orElse(null);
        if(productVariant == null) {
            throw new IllegalArgumentException("Product variant not found");
        }
        if(productVariant.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock to remove");
        }
        productVariant.setQuantity(productVariant.getQuantity() - quantity);
        return productVariantRepository.save(productVariant);
    }
    public ProductVariant deactivate(Long id) {
        ProductVariant existingProductVariant = productVariantRepository.findById(id).orElse(null);
        if(existingProductVariant != null) {
            existingProductVariant.setActive(false);
            return productVariantRepository.save(existingProductVariant);
        }
        return null;
    }
    public ProductVariant activate(Long id) {
        ProductVariant existingProductVariant = productVariantRepository.findById(id).orElse(null);
        if(existingProductVariant != null) {
            existingProductVariant.setActive(true);
            return productVariantRepository.save(existingProductVariant);
        }
        return null;
    }
}
