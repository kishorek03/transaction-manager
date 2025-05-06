package com.transaction.controller;

import com.transaction.dto.ApiResponse;
import com.transaction.dto.AddOnDTO;
import com.transaction.service.AddOnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/addons")
@RequiredArgsConstructor
public class AddOnController {

    private final AddOnService addOnService;

    @PostMapping
    public ResponseEntity<ApiResponse<AddOnDTO>> create(@RequestBody AddOnDTO addOnDTO) {
        log.info("Creating AddOn: {}", addOnDTO.name());
        AddOnDTO saved = addOnService.create(addOnDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("success", "AddOn created", saved));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddOnDTO>>> getAll() {
        log.info("Retrieving all AddOns");
        List<AddOnDTO> list = addOnService.findAll();
        return ResponseEntity.ok(new ApiResponse<>("success", "AddOns retrieved", list));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddOnDTO>> getById(@PathVariable Long id) {
        log.info("Retrieving AddOn with ID: {}", id);
        AddOnDTO found = addOnService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "AddOn found", found));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddOnDTO>> update(@PathVariable Long id, @RequestBody AddOnDTO updatedDTO) {
        log.info("Updating AddOn with ID: {}", id);
        AddOnDTO updated = addOnService.update(id, updatedDTO);
        return ResponseEntity.ok(new ApiResponse<>("success", "AddOn updated", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        log.info("Deleting AddOn with ID: {}", id);
        addOnService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "AddOn deleted", null));
    }
}
