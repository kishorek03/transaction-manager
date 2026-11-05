package com.transaction.controller;

import com.transaction.dto.ApiResponse;
import com.transaction.dto.ShopHolidayDTO;
import com.transaction.service.ShopHolidayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/holidays")
@RequiredArgsConstructor
public class ShopHolidayController {

    private final ShopHolidayService shopHolidayService;

    @PostMapping
    public ResponseEntity<ApiResponse<ShopHolidayDTO>> createHoliday(@Valid @RequestBody ShopHolidayDTO dto) {
        ShopHolidayDTO saved = shopHolidayService.createHoliday(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("success", "Shop holiday created successfully", saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ShopHolidayDTO>> updateHoliday(
            @PathVariable Long id,
            @RequestBody ShopHolidayDTO dto
    ) {
        ShopHolidayDTO updated = shopHolidayService.updateHoliday(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("success", "Shop holiday updated successfully", updated));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ShopHolidayDTO>>> getHolidays(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from
    ) {
        List<ShopHolidayDTO> list = shopHolidayService.getHolidays(from);
        return ResponseEntity.ok(new ApiResponse<>("success", "Shop holidays fetched successfully", list));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHoliday(@PathVariable Long id) {
        shopHolidayService.deleteHoliday(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Shop holiday deleted successfully", null));
    }
}
