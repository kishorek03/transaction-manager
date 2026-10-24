package com.transaction.service;

import com.transaction.dto.ExpenseDTO;
import com.transaction.dto.mapper.ExpenseMapper;
import com.transaction.entity.*;
import com.transaction.exception.BadRequestException;
import com.transaction.repository.CategoryRepository;
import com.transaction.repository.ExpenseItemRepository;
import com.transaction.repository.ExpenseRepository;
import com.transaction.repository.PaymentMethodRepository;
import com.transaction.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final UnitRepository unitRepository;
    private final ExpenseItemRepository expenseItemRepository;
    private final ExpenseMapper expenseMapper;

    @Transactional
    public ExpenseDTO createExpense(ExpenseDTO dto) {
        ExpenseItem expenseItem = null;
        Long categoryId = dto.categoryId();
        Long unitId = dto.unitId();
        String itemName = dto.item();

        if (dto.expenseItemId() != null) {
            expenseItem = expenseItemRepository.findById(dto.expenseItemId())
                    .orElseThrow(() -> new BadRequestException("Expense item not found with ID: " + dto.expenseItemId()));

            if (categoryId == null && expenseItem.getCategory() != null) {
                categoryId = expenseItem.getCategory().getId();
            }
            if (unitId == null && expenseItem.getDefaultUnit() != null) {
                unitId = expenseItem.getDefaultUnit().getId();
            }
            if (itemName == null || itemName.isBlank()) {
                itemName = expenseItem.getName();
            }
        }

        if (categoryId == null) {
            throw new BadRequestException("Category ID cannot be null");
        }
        if (unitId == null) {
            throw new BadRequestException("Unit ID cannot be null");
        }
        if (dto.paymentMethodId() == null) {
            throw new BadRequestException("Payment method ID cannot be null");
        }
        if (itemName == null || itemName.isBlank()) {
            throw new BadRequestException("Item name cannot be empty");
        }

        final Long resolvedCategoryId = categoryId;
        final Long resolvedUnitId = unitId;

        Category category = categoryRepository.findById(resolvedCategoryId)
                .orElseThrow(() -> new BadRequestException("Invalid category ID: " + resolvedCategoryId));
        PaymentMethod paymentMethod = paymentMethodRepository.findById(dto.paymentMethodId())
                .orElseThrow(() -> new BadRequestException("Invalid payment method ID: " + dto.paymentMethodId()));
        Unit unit = unitRepository.findById(resolvedUnitId)
                .orElseThrow(() -> new BadRequestException("Invalid unit ID: " + resolvedUnitId));

        Expense expense = expenseMapper.toEntity(dto);
        expense.setItem(itemName.trim());
        expense.setCategory(category);
        expense.setPaymentMethod(paymentMethod);
        expense.setUnit(unit);
        expense.setExpenseItem(expenseItem);

        Expense saved = expenseRepository.save(expense);
        log.info("Created expense: {} (ID: {})", saved.getItem(), saved.getId());

        return expenseMapper.toDto(saved);
    }

    public List<ExpenseDTO> getAllExpenses(LocalDateTime start, LocalDateTime end) {
        List<Expense> expenseList = expenseRepository.findByCreatedAtGreaterThanEqualAndCreatedAtLessThan(start, end);
        return expenseList.stream()
                .map(expenseMapper::toDto)
                .collect(Collectors.toList());
    }
}
