package com.transaction.controller;

import com.transaction.dto.ApiResponse;
import com.transaction.dto.SaleDTO;
import com.transaction.dto.mapper.SaleMapper;
import com.transaction.entity.Sale;
import com.transaction.service.SaleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
@Slf4j
@RequiredArgsConstructor
public class SaleController {

    private final SaleService salesService;
    private final SaleMapper saleMapper;

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
    @GetMapping("/fetch")
    public List<SaleDTO> fetchSales(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long flavourId,
            @RequestParam(required = false) Boolean parcel
    ) {
        return salesService.filterSales(startDate, endDate, productId, flavourId, parcel)
                .stream()
                .map(saleMapper::toDto)
                .toList();
    }

}
