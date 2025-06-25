package com.transaction.repository;

import com.transaction.entity.DailyCashBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyCashBalanceRepository extends JpaRepository<DailyCashBalance, Long> {
    Optional<DailyCashBalance> findByBusinessDate(LocalDate businessDate);
}
