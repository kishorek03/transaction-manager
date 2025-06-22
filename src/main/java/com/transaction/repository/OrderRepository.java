package com.transaction.repository;

import com.transaction.dto.OrderSummaryDTO;
import com.transaction.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT new com.transaction.dto.OrderSummaryDTO(o.id, o.totalAmount, o.paymentMethod.name) " +
            "FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<OrderSummaryDTO> findOrderSummariesBetweenDates(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
