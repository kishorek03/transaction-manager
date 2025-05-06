package com.transaction.controller;

import com.transaction.dto.ApiResponse;
import com.transaction.dto.UnitDTO;
import com.transaction.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class UnitController {

    private final UnitService unitService;

    @PostMapping
    public ResponseEntity<ApiResponse<UnitDTO>> create(@RequestBody UnitDTO unitDTO) {
        log.info("Creating Unit");
        UnitDTO saved = unitService.create(unitDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("success", "Unit created", saved));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UnitDTO>>> getAll() {
        log.info("Retrieving all Units");
        List<UnitDTO> list = unitService.findAll();
        return ResponseEntity.ok(new ApiResponse<>("success", "Units retrieved", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UnitDTO>> getById(@PathVariable Long id) {
        log.info("Retrieving Unit with ID: {}", id);
        UnitDTO found = unitService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Unit found", found));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UnitDTO>> update(@PathVariable Long id, @RequestBody UnitDTO updatedDTO) {
        log.info("Updating Unit with ID: {}", id);
        UnitDTO updated = unitService.update(id, updatedDTO);
        return ResponseEntity.ok(new ApiResponse<>("success", "Unit updated", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        log.info("Deleting Unit with ID: {}", id);
        unitService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Unit deleted", null));
    }
}
