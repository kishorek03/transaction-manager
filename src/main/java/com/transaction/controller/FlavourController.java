package com.transaction.controller;

import com.transaction.dto.ApiResponse;
import com.transaction.dto.FlavourDTO;
import com.transaction.service.FlavourService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flavours")
public class FlavourController {

    private final FlavourService flavourService;

    public FlavourController(FlavourService flavourService) {
        this.flavourService = flavourService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FlavourDTO>> create(@RequestBody FlavourDTO dto) {
        FlavourDTO saved = flavourService.create(dto);
        return ResponseEntity.status(201)
                .body(new ApiResponse<>("success", "Flavour created", saved));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<FlavourDTO>>> getAll() {
        List<FlavourDTO> list = flavourService.getAll();
        return ResponseEntity.ok(new ApiResponse<>("success", "All flavours fetched", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FlavourDTO>> getById(@PathVariable Long id) {
        FlavourDTO dto = flavourService.getById(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Flavour found", dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FlavourDTO>> update(@PathVariable Long id, @RequestBody FlavourDTO dto) {
        FlavourDTO updated = flavourService.update(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("success", "Flavour updated", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        flavourService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Flavour deleted", null));
    }
}
