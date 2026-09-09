package com.boutique.pos.service;

import com.boutique.pos.model.Product;
import com.boutique.pos.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public Product create(Product product)
    {
        return productRepository.save(product);
    }
    public List<Product> getAll()
    {
        return productRepository.findAll();
    }
    public Product getById(Long id)
    {
        return productRepository.findById(id).orElse(null);
    }
    public List<Product> getActiveProducts()
    {
        return productRepository.findByActiveTrue(true);
    }
    public List<Product> search(String name)
    {
        return productRepository.findByNameContainingIgnoreCase(name);
    }
    public List<Product> getByCategory(Long categoryId)
    {
        return productRepository.findByCategoryId(categoryId);
    }
    public List<Product> getByBrand(Long brandId)
    {
        return productRepository.findByBrandId(brandId);
    }
    public Product update(Long id, Product product)
    {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if(existingProduct != null)
        {
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setBrand(product.getBrand());
            existingProduct.setPurchasePrice(product.getPurchasePrice());
            existingProduct.setSellingPrice(product.getSellingPrice());
            existingProduct.setActive(product.getActive());
            return productRepository.save(existingProduct);
        }
        return null;
    }
    public void deactivate(Long id)
    {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if(existingProduct != null)
        {
            existingProduct.setActive(false);
            productRepository.save(existingProduct);
        }
    }
    public void activate(Long id)
    {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if(existingProduct != null)
        {
            existingProduct.setActive(true);
            productRepository.save(existingProduct);
        }
    }
    public List<Product> searchProducts(String query)
    {
        return productRepository.findByNameContainingIgnoreCase(query);
    }
    public List<Product> createProducts(List<Product> products) {
        return productRepository.saveAll(products);
    }
}
