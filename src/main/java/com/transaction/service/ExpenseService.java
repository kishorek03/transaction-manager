package com.transaction.service;

import com.transaction.entity.Expense;
import com.transaction.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    public Expense findById(Long id) {
        return expenseRepository.findById(id).orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));
    }

    public Expense update(Long id, Expense expenseDetails) {
        Expense expense = findById(id);
        expense.setItem(expenseDetails.getItem());
        expense.setQuantity(expenseDetails.getQuantity());
        expense.setAmount(expenseDetails.getAmount());
        expense.setCategory(expenseDetails.getCategory());
        expense.setPaymentMethod(expenseDetails.getPaymentMethod());
        return expenseRepository.save(expense);
    }

    public void delete(Long id) {
        expenseRepository.deleteById(id);
    }
}
