package com.transaction.repository;

import com.transaction.entity.CashMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface CashMovementRepository extends JpaRepository<CashMovement, Long> {
    @Query("""
    SELECT SUM(CASE 
                 WHEN c.type = 'IN' THEN c.amount
                 WHEN c.type = 'ADJUSTMENT' THEN c.amount
                 WHEN c.type = 'OUT' THEN -c.amount
                 WHEN c.type = 'TRANSFER' THEN -c.amount
                 ELSE 0 
               END)
    FROM CashMovement c WHERE DATE(c.createdAt) = :date
""")
    Optional<BigDecimal> getNetCashMovementByDate(@Param("date") LocalDate date);

}
