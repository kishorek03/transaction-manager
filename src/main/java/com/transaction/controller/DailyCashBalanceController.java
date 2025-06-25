package com.transaction.controller;

import com.transaction.dto.ApiResponse;
import com.transaction.dto.DailyCashBalanceDTO;
import com.transaction.service.DailyCashBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/daily-cash-balance")
@RequiredArgsConstructor
public class DailyCashBalanceController {

    private final DailyCashBalanceService service;

    @PostMapping
    public ResponseEntity<ApiResponse<DailyCashBalanceDTO>> create(@RequestBody DailyCashBalanceDTO dto) {
        DailyCashBalanceDTO created = service.createOpeningBalance(dto);
        ApiResponse<DailyCashBalanceDTO> response = new ApiResponse<>("success", "Opening balance created", created);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{date}")
    public ResponseEntity<ApiResponse<DailyCashBalanceDTO>> getByDate(@PathVariable("date") String date) {
        return service.getBalanceByDate(LocalDate.parse(date))
                .map(balance -> new ApiResponse<>("success", "Balance found for " + date, balance))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(new ApiResponse<>("error", "No balance found for " + date, null, "Not found")));
    }

    @GetMapping("/today")
    public ResponseEntity<ApiResponse<BigDecimal>> getTodayBalance() {
        LocalDate today = LocalDate.now();
        BigDecimal amount = service.getBalanceByDate(today)
                .map(DailyCashBalanceDTO::amount)
                .orElse(BigDecimal.ZERO);
        ApiResponse<BigDecimal> response = new ApiResponse<>("success", "Today's balance", amount);
        return ResponseEntity.ok(response);
    }
}
