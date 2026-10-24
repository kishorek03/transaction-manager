package com.transaction.controller;

import com.transaction.dto.ApiResponse;
import com.transaction.dto.ExpenseItemDTO;
import com.transaction.service.ExpenseItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/expense-items")
@RequiredArgsConstructor
public class ExpenseItemController {

    private final ExpenseItemService expenseItemService;

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseItemDTO>> createExpenseItem(@Valid @RequestBody ExpenseItemDTO dto) {
        log.info("Creating expense item: {}", dto.name());
        ExpenseItemDTO created = expenseItemService.createExpenseItem(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("success", "Expense item created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpenseItemDTO>>> getAllExpenseItems(
            @RequestParam(required = false, defaultValue = "true") Boolean activeOnly,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search) {
        log.info("Fetching expense items (activeOnly={}, categoryId={}, search={})", activeOnly, categoryId, search);
        List<ExpenseItemDTO> items = expenseItemService.getAllExpenseItems(activeOnly, categoryId, search);
        return ResponseEntity.ok(new ApiResponse<>("success", "Expense items fetched successfully", items));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseItemDTO>> getExpenseItemById(@PathVariable Long id) {
        log.info("Fetching expense item by id: {}", id);
        ExpenseItemDTO item = expenseItemService.getExpenseItemById(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Expense item fetched successfully", item));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseItemDTO>> updateExpenseItem(
            @PathVariable Long id,
            @Valid @RequestBody ExpenseItemDTO dto) {
        log.info("Updating expense item ID: {}", id);
        ExpenseItemDTO updated = expenseItemService.updateExpenseItem(id, dto);
        return ResponseEntity.ok(new ApiResponse<>("success", "Expense item updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpenseItem(@PathVariable Long id) {
        log.info("Deleting (soft-delete) expense item ID: {}", id);
        expenseItemService.deleteExpenseItem(id);
        return ResponseEntity.ok(new ApiResponse<>("success", "Expense item deleted successfully", null));
    }
}
