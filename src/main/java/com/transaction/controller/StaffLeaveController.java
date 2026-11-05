package com.transaction.controller;

import com.transaction.dto.ApiResponse;
import com.transaction.dto.StaffLeaveDTO;
import com.transaction.service.StaffLeaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class StaffLeaveController {

    private final StaffLeaveService staffLeaveService;

    @PostMapping
    public ResponseEntity<ApiResponse<StaffLeaveDTO>> recordLeave(@Valid @RequestBody StaffLeaveDTO dto) {
        StaffLeaveDTO saved = staffLeaveService.recordLeave(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("success", "Staff leave recorded successfully", saved));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<StaffLeaveDTO>>> getLeaves(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String staffName,
            @RequestParam(required = false) String month
    ) {
        List<StaffLeaveDTO> list = staffLeaveService.getLeaves(from, to, staffName, month);
        return ResponseEntity.ok(new ApiResponse<>("success", "Staff leaves fetched successfully", list));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StaffLeaveDTO>> updateLeave(
            @PathVariable Long id,
            @RequestBody StaffLeaveDTO dto
    ) {
        StaffLeaveDTO updated = staffLeaveService.updateLeave(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("success", "Staff leave updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLeave(@PathVariable Long id) {
        staffLeaveService.deleteLeave(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Staff leave deleted successfully", null));
    }
}
