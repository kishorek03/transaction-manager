package com.transaction.service;

import com.transaction.dto.SaleDTO;
import com.transaction.entity.Flavour;
import com.transaction.entity.Product;
import com.transaction.entity.Sale;
import com.transaction.repository.ProductRepository;
import com.transaction.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository salesRepository;
    private final ProductRepository productRepository;

    public Sale save(Sale sale) {
        return salesRepository.save(sale);
    }

    public List<Sale> findAll() {
        return salesRepository.findAll();
    }

    public Sale findById(Long id) {
        return salesRepository.findById(id).orElseThrow(() -> new RuntimeException("Sale not found with id: " + id));
    }

    public Sale update(Long id, Sale updatedSale) {
        Sale sale = findById(id);
        sale.setProduct(updatedSale.getProduct());
        sale.setQuantity(updatedSale.getQuantity());
        sale.setAmount(updatedSale.getAmount());
        sale.setParcel(updatedSale.getParcel());
        return salesRepository.save(sale);
    }

    public void delete(Long id) {
        salesRepository.deleteById(id);
    }


    public BigDecimal calculateAmount(SaleDTO salesDTO) {
        Product product = productRepository.findById(salesDTO.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        BigDecimal productPrice = product.getUnitPrice();
        BigDecimal flavourPrice = product.getFlavours().stream()
                .filter(flavour -> flavour.getId().equals(salesDTO.flavourId()))
                .findFirst()
                .map(Flavour::getPrice)
                .orElse(BigDecimal.ZERO);


        BigDecimal parcelPrice = salesDTO.parcel() ? product.getParcelPrice().multiply(salesDTO.quantity()) : BigDecimal.ZERO;

        // Calculate the total amount: (product price + flavour price + parcel price) * quantity
        return (productPrice.add(flavourPrice)).multiply(salesDTO.quantity()).add(parcelPrice);
    }
    public List<Sale> filterSales(LocalDateTime startDate, LocalDateTime endDate, Long productId, Long flavourId, Boolean parcel) {
        return salesRepository.findByFilters(startDate, endDate, productId, flavourId, parcel);
    }

}
