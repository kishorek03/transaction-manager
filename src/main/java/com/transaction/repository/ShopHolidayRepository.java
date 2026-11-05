package com.transaction.repository;

import com.transaction.entity.ShopHoliday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShopHolidayRepository extends JpaRepository<ShopHoliday, Long> {

    List<ShopHoliday> findAllByOrderByHolidayDateAsc();

    List<ShopHoliday> findByHolidayDateGreaterThanEqualOrderByHolidayDateAsc(LocalDate fromDate);
}
