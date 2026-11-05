package com.transaction.repository;

import com.transaction.entity.StaffLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StaffLeaveRepository extends JpaRepository<StaffLeave, Long> {

    List<StaffLeave> findAllByOrderByLeaveDateDesc();

    List<StaffLeave> findByLeaveDateBetweenOrderByLeaveDateDesc(LocalDate start, LocalDate end);

    List<StaffLeave> findByStaffNameIgnoreCaseAndLeaveDateBetweenOrderByLeaveDateDesc(String staffName, LocalDate start, LocalDate end);

    long countByStaffNameIgnoreCaseAndLeaveDateBetween(String staffName, LocalDate start, LocalDate end);
}
