package com.transaction.repository;

import com.transaction.dto.OrderSummaryDTO;
import com.transaction.dto.projection.PeriodAggregateProjection;
import com.transaction.dto.projection.TotalsAggregateProjection;
import com.transaction.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT new com.transaction.dto.OrderSummaryDTO(o.id, o.totalAmount, o.paymentMethod.name) " +
            "FROM Order o WHERE o.createdAt >= :startDate AND o.createdAt < :endDate")
    List<OrderSummaryDTO> findOrderSummariesBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query(value = """
        SELECT 
            COALESCE(SUM(o.total_amount), 0) AS total,
            COUNT(o.id) AS count,
            COALESCE(SUM(CASE WHEN p.name = 'CASH' THEN o.total_amount ELSE 0 END), 0) AS cash,
            COALESCE(SUM(CASE WHEN p.name = 'UPI' THEN o.total_amount ELSE 0 END), 0) AS upi
        FROM orders o
        LEFT JOIN payment_method p ON o.payment_method_id = p.id
        WHERE o.created_at >= :start AND o.created_at < :end
    """, nativeQuery = true)
    TotalsAggregateProjection getSalesTotalsBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query(value = """
        SELECT 
            DATE_FORMAT(o.created_at, :dateFormat) AS period,
            COALESCE(SUM(o.total_amount), 0) AS total,
            COUNT(o.id) AS count
        FROM orders o
        WHERE o.created_at >= :start AND o.created_at < :end
        GROUP BY DATE_FORMAT(o.created_at, :dateFormat)
        ORDER BY period ASC
    """, nativeQuery = true)
    List<PeriodAggregateProjection> getSalesByPeriod(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("dateFormat") String dateFormat
    );
}
