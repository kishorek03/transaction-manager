package com.transaction.service;

import com.transaction.dto.ProductDTO;
import com.transaction.dto.mapper.ProductMapper;
import com.transaction.entity.AddOn;
import com.transaction.entity.Flavour;
import com.transaction.entity.Product;
import com.transaction.entity.Unit;
import com.transaction.exception.BadRequestException;
import com.transaction.repository.AddOnRepository;
import com.transaction.repository.FlavourRepository;
import com.transaction.repository.ProductRepository;
import com.transaction.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductDTO> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toDto)
                .toList();
    }

    public ProductDTO createProduct(ProductDTO dto) {
        Product product = productMapper.toEntity(dto);
        Product saved = productRepository.save(product);
        return productMapper.toDto(saved);
    }

    public ProductDTO update(Long id, ProductDTO dto) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Product not found with id " + id));

        Product updatedProduct = productMapper.toEntity(dto);
        updatedProduct.setId(existing.getId()); // Ensure the ID is preserved

        Product saved = productRepository.save(updatedProduct);
        return productMapper.toDto(saved);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new BadRequestException("Product not found with id " + id);
        }
        productRepository.deleteById(id);
    }
}
