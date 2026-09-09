package com.boutique.pos.service;

import com.boutique.pos.model.Brand;
import com.boutique.pos.repository.BrandRepository;
import org.springframework.stereotype.Service;

@Service

public class BrandService {
    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }
    public Brand create(Brand brand) {
        return brandRepository.save(brand);
    }
}
