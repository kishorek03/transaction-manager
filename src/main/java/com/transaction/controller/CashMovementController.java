package com.transaction.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.transaction.dto.ApiResponse;
import com.transaction.dto.CashMovementDTO;
import com.transaction.service.CashMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cash-movements")
@RequiredArgsConstructor
public class CashMovementController {

    private final CashMovementService service;

    @PostMapping
    public ResponseEntity<ApiResponse<CashMovementDTO>> create(@RequestBody CashMovementDTO dto) {
        CashMovementDTO saved = service.createMovement(dto);
        ApiResponse<CashMovementDTO> response = new ApiResponse<>("success", "Cash movement recorded", saved);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CashMovementDTO>>> getAll() {
        List<CashMovementDTO> list = service.getAllMovements();
        ApiResponse<List<CashMovementDTO>> response = new ApiResponse<>("success", "All cash movements fetched", list);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/net-movement")
    public ResponseEntity<ApiResponse<BigDecimal>> getNetCashMovement(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        BigDecimal netMovement = service.getNetCashMovementByDate(date);
        ApiResponse<BigDecimal> response = new ApiResponse<>("success", "Net cash movement for " + date, netMovement);
        return ResponseEntity.ok(response);
    }
}
