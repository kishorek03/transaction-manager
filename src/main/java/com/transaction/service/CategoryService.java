package com.transaction.service;

import com.transaction.entity.Category;
import com.transaction.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    public Category update(Long id, Category updatedCategory) {
        Category existing = findById(id);
        existing.setCategoryName(updatedCategory.getCategoryName());
        existing.setCategoryType(updatedCategory.getCategoryType());
        existing.setIsRecurring(updatedCategory.getIsRecurring());
        existing.setDescription(updatedCategory.getDescription());
        return categoryRepository.save(existing);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
