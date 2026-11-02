package com.transaction.service;

import com.transaction.dto.ProductDTO;
import com.transaction.dto.mapper.ProductMapper;
import com.transaction.entity.Product;
import com.transaction.exception.BadRequestException;
import com.transaction.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductDTO> findAll() {
        return findAll(false, false);
    }

    public List<ProductDTO> findAll(Boolean activeOnly, Boolean all) {
        List<Product> products;
        if (Boolean.TRUE.equals(activeOnly) && !Boolean.TRUE.equals(all)) {
            products = productRepository.findByIsActiveTrue();
        } else {
            products = productRepository.findAll();
        }
        return products.stream()
                .map(productMapper::toDto)
                .toList();
    }

    public ProductDTO createProduct(ProductDTO dto) {
        Product product = productMapper.toEntity(dto);
        if (product.getIsActive() == null) {
            product.setIsActive(true);
        }
        Product saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    public ProductDTO update(Long id, ProductDTO dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Product not found with id " + id));

        Product updatedProduct = productMapper.toEntity(dto);
        updatedProduct.setId(existing.getId()); // Ensure the ID is preserved
        if (updatedProduct.getIsActive() == null) {
            updatedProduct.setIsActive(existing.getIsActive());
        }

        Product saved = productRepository.save(updatedProduct);
        return productMapper.toDto(saved);
    }

    public void delete(Long id) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Product not found with id " + id));
        // Soft delete to preserve historical order/sales relationships
        existing.setIsActive(false);
        productRepository.save(existing);
    }
}
