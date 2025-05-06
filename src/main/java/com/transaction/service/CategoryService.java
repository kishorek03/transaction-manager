package com.transaction.service;

import com.transaction.dto.CategoryDTO;
import com.transaction.dto.mapper.CategoryMapper;
import com.transaction.entity.Category;
import com.transaction.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryDTO save(CategoryDTO dto) {
        Category saved = categoryRepository.save(categoryMapper.toEntity(dto));
        return categoryMapper.toDto(saved);
    }

    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    public CategoryDTO findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return categoryMapper.toDto(category);
    }

    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        existing.setCategoryName(dto.categoryName());
        existing.setCategoryType(dto.categoryType());
        existing.setRecurring(dto.isRecurring());
        existing.setDescription(dto.description());
        Category updated = categoryRepository.save(existing);
        return categoryMapper.toDto(updated);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
