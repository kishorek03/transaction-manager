package com.transaction.controller;

import com.transaction.dto.ApiResponse;
import com.transaction.dto.SaleDTO;
import com.transaction.entity.Sale;
import com.transaction.service.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
@Slf4j
@RequiredArgsConstructor
public class SaleController {

    @Autowired
    private SaleService salesService;

    @PostMapping
    public Sale create(@RequestBody Sale sale) {
        return salesService.save(sale);
    }

    @GetMapping
    public List<Sale> getAll() {
        return salesService.findAll();
    }

    @GetMapping("/{id}")
    public Sale getById(@PathVariable Long id) {
        return salesService.findById(id);
    }

    @PutMapping("/{id}")
    public Sale update(@PathVariable Long id, @RequestBody Sale updatedSale) {
        return salesService.update(id, updatedSale);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        salesService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/calculateAmount")
    public ResponseEntity<ApiResponse<BigDecimal>> calculateAmount(@RequestBody SaleDTO saleDTO) {
        log.info("Calculating amount for sales with productId: {}, quantity: {}, parcel: {}",
                saleDTO.productId(), saleDTO.quantity(), saleDTO.parcel());

        BigDecimal amount = salesService.calculateAmount(saleDTO);

        return ResponseEntity.ok(new ApiResponse<>("success", "Amount calculated", amount));
    }
}
