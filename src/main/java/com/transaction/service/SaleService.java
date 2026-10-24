package com.transaction.service;

import com.transaction.dto.SaleDTO;
import com.transaction.dto.SalesSearchResponseDTO;
import com.transaction.dto.SalesUnitSummaryDTO;
import com.transaction.dto.UnitQuantityCountDTO;
import com.transaction.dto.mapper.SaleMapper;
import com.transaction.entity.Flavour;
import com.transaction.entity.Product;
import com.transaction.entity.Sale;
import com.transaction.entity.Unit;
import com.transaction.repository.ProductRepository;
import com.transaction.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository salesRepository;
    private final ProductRepository productRepository;
    private final SaleMapper saleMapper;

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

    public SalesUnitSummaryDTO calculateUnitSummary(List<Sale> sales) {
        if (sales == null || sales.isEmpty()) {
            return new SalesUnitSummaryDTO(
                    0L,
                    BigDecimal.ZERO,
                    true,
                    null,
                    null,
                    null,
                    BigDecimal.ZERO,
                    Collections.emptyList()
            );
        }

        BigDecimal overallTotalAmount = BigDecimal.ZERO;
        long overallCount = sales.size();

        Map<Long, UnitQuantityAggregator> map = new LinkedHashMap<>();

        for (Sale sale : sales) {
            BigDecimal amt = (sale.getAmount() != null) ? sale.getAmount() : BigDecimal.ZERO;
            overallTotalAmount = overallTotalAmount.add(amt);

            BigDecimal qty = (sale.getQuantity() != null)
                    ? BigDecimal.valueOf(sale.getQuantity())
                    : BigDecimal.ZERO;

            Product p = sale.getProduct();
            Unit u = (p != null) ? p.getUnit() : null;

            Long unitId = (u != null && u.getId() != null) ? u.getId() : -1L;
            String unitName = (u != null && u.getName() != null) ? u.getName() : "Unknown";
            String unitAbbr = (u != null && u.getAbbreviation() != null) ? u.getAbbreviation() : "";

            UnitQuantityAggregator agg = map.computeIfAbsent(unitId, k -> new UnitQuantityAggregator(unitId, unitName, unitAbbr));
            agg.add(qty, amt);
        }

        List<UnitQuantityCountDTO> unitQuantities = new ArrayList<>();
        for (UnitQuantityAggregator agg : map.values()) {
            unitQuantities.add(new UnitQuantityCountDTO(
                    agg.unitId == -1L ? null : agg.unitId,
                    agg.unitName,
                    agg.unitAbbreviation,
                    agg.totalQuantity,
                    agg.saleCount,
                    agg.totalAmount
            ));
        }

        boolean allSameUnit = (map.size() <= 1);
        Long sameUnitId = null;
        String sameUnitName = null;
        String sameUnitAbbr = null;
        BigDecimal sameUnitTotalQuantity = null;

        if (allSameUnit && !map.isEmpty()) {
            UnitQuantityAggregator only = map.values().iterator().next();
            sameUnitId = only.unitId == -1L ? null : only.unitId;
            sameUnitName = only.unitName;
            sameUnitAbbr = only.unitAbbreviation;
            sameUnitTotalQuantity = only.totalQuantity;
        }

        return new SalesUnitSummaryDTO(
                overallCount,
                overallTotalAmount,
                allSameUnit,
                sameUnitId,
                sameUnitName,
                sameUnitAbbr,
                sameUnitTotalQuantity,
                unitQuantities
        );
    }

    public SalesSearchResponseDTO getSalesSearchWithSummary(
            LocalDateTime start, LocalDateTime end, Long productId, Long flavourId, Boolean parcel) {
        List<Sale> sales = filterSales(start, end, productId, flavourId, parcel);
        List<SaleDTO> saleDTOs = sales.stream().map(saleMapper::toDto).toList();
        SalesUnitSummaryDTO summary = calculateUnitSummary(sales);
        return new SalesSearchResponseDTO(saleDTOs, summary);
    }

    private static class UnitQuantityAggregator {
        Long unitId;
        String unitName;
        String unitAbbreviation;
        BigDecimal totalQuantity = BigDecimal.ZERO;
        Long saleCount = 0L;
        BigDecimal totalAmount = BigDecimal.ZERO;

        UnitQuantityAggregator(Long unitId, String unitName, String unitAbbreviation) {
            this.unitId = unitId;
            this.unitName = unitName;
            this.unitAbbreviation = unitAbbreviation;
        }

        void add(BigDecimal qty, BigDecimal amt) {
            this.totalQuantity = this.totalQuantity.add(qty);
            this.saleCount++;
            this.totalAmount = this.totalAmount.add(amt);
        }
    }
}
