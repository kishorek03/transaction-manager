package com.transaction.repository;

import com.transaction.dto.projection.CategoryAggregateProjection;
import com.transaction.dto.projection.PeriodAggregateProjection;
import com.transaction.dto.projection.TotalsAggregateProjection;
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

    List<Expense> findByCreatedAtGreaterThanEqualAndCreatedAtLessThan(LocalDateTime start, LocalDateTime end);

    @Query("""
        SELECT SUM(e.amount)
        FROM Expense e
        WHERE DATE(e.createdAt) = :date
        AND e.paymentMethod.name = 'CASH'
    """)
    Optional<BigDecimal> getTotalExpensesByDateAndCash(@Param("date") LocalDate date);

    @Query(value = """
        SELECT 
            COALESCE(SUM(e.amount), 0) AS total,
            COUNT(e.id) AS count,
            COALESCE(SUM(CASE WHEN p.name = 'CASH' THEN e.amount ELSE 0 END), 0) AS cash,
            COALESCE(SUM(CASE WHEN p.name = 'UPI' THEN e.amount ELSE 0 END), 0) AS upi
        FROM expense e
        LEFT JOIN payment_method p ON e.payment_method_id = p.id
        WHERE e.created_at >= :start AND e.created_at < :end
    """, nativeQuery = true)
    TotalsAggregateProjection getExpenseTotalsBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = """
        SELECT 
            DATE_FORMAT(e.created_at, :dateFormat) AS period,
            COALESCE(SUM(e.amount), 0) AS total,
            COUNT(e.id) AS count
        FROM expense e
        WHERE e.created_at >= :start AND e.created_at < :end
        GROUP BY DATE_FORMAT(e.created_at, :dateFormat)
        ORDER BY period ASC
    """, nativeQuery = true)
    List<PeriodAggregateProjection> getExpensesByPeriod(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("dateFormat") String dateFormat
    );

    @Query(value = """
        SELECT 
            c.id AS categoryId,
            c.category_name AS categoryName,
            COALESCE(SUM(e.amount), 0) AS total
        FROM expense e
        JOIN category c ON e.category_id = c.id
        WHERE e.created_at >= :start AND e.created_at < :end
        GROUP BY c.id, c.category_name
        ORDER BY total DESC
    """, nativeQuery = true)
    List<CategoryAggregateProjection> getCategoryExpensesBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
