package com.transaction.service;

import com.transaction.dto.ExpenseDTO;
import com.transaction.dto.mapper.ExpenseMapper;
import com.transaction.entity.Category;
import com.transaction.entity.Expense;
import com.transaction.entity.PaymentMethod;
import com.transaction.repository.CategoryRepository;
import com.transaction.repository.ExpenseRepository;
import com.transaction.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ExpenseMapper expenseMapper;

    public ExpenseDTO createExpense(ExpenseDTO dto) {
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid category ID"));
        PaymentMethod paymentMethod = paymentMethodRepository.findById(dto.paymentMethodId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid payment method ID"));

        Expense expense = expenseMapper.toEntity(dto);

        Expense saved = expenseRepository.save(expense);

        return expenseMapper.toDto(saved);
    }
    public List<ExpenseDTO> getAllExpenses(LocalDateTime start, LocalDateTime end) {
        List<Expense> expenseList = expenseRepository.findByCreatedAtBetween(start, end);
        return expenseList.stream()
                .map(expenseMapper::toDto)
                .collect(Collectors.toList());
    }


}

