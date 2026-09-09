package com.boutique.pos.service;

import com.boutique.pos.model.Category;
import com.boutique.pos.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    public Category create(Category category) {
        return categoryRepository.save(category);
    }
}
