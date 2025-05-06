package com.transaction.controller;

import com.transaction.dto.ApiResponse;
import com.transaction.dto.ProductDTO;
import com.transaction.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDTO>> create(@RequestBody ProductDTO dto) {
        log.info("Creating new product: {}", dto.name());
        ProductDTO responseDto = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("success", "Product created", responseDto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAll() {
        log.info("Fetching all products");
        List<ProductDTO> productDTOs = productService.findAll();
        return ResponseEntity.ok(new ApiResponse<>("success", "Products retrieved", productDTOs));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        log.info("Updating product with ID: {}", id);
        ProductDTO updated = productService.update(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("success", "Product updated", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        log.info("Deleting product with ID: {}", id);
        productService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Product deleted", null));
    }
}
