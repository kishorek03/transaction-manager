package com.transaction.repository;

import com.transaction.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    @Query("""
    SELECT SUM(e.amount)
    FROM Expense e
    WHERE DATE(e.createdAt) = :date
    AND e.paymentMethod.name = 'CASH'
""")
    Optional<BigDecimal> getTotalExpensesByDateAndCash(@Param("date") LocalDate date);

}
