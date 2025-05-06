package com.transaction.controller;

import com.transaction.dto.ApiResponse;
import com.transaction.dto.CategoryDTO;
import com.transaction.entity.Category;
import com.transaction.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDTO>> create(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO saved = categoryService.save(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("success", "Category created successfully", saved));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryDTO>>> getAll() {
        List<CategoryDTO> categories = categoryService.findAll();
        return ResponseEntity.ok(new ApiResponse<>("success", "Categories fetched successfully", categories));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> getById(@PathVariable Long id) {
        CategoryDTO dto = categoryService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Category fetched successfully", dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto) {
        CategoryDTO updated = categoryService.update(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("success", "Category updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Category deleted successfully", null));
    }
}
