package com.transaction.repository;

import com.transaction.entity.ExpenseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseItemRepository extends JpaRepository<ExpenseItem, Long> {

    List<ExpenseItem> findByIsActiveTrueOrderByNameAsc();

    List<ExpenseItem> findAllByOrderByNameAsc();

    @Query("""
        SELECT e FROM ExpenseItem e
        WHERE (:activeOnly = false OR e.isActive = true)
        AND (:categoryId IS NULL OR e.category.id = :categoryId)
        AND (:search IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :search, '%'))
             OR LOWER(e.tamilName) LIKE LOWER(CONCAT('%', :search, '%')))
        ORDER BY e.name ASC
    """)
    List<ExpenseItem> findByFilters(
            @Param("activeOnly") boolean activeOnly,
            @Param("categoryId") Long categoryId,
            @Param("search") String search
    );
}
