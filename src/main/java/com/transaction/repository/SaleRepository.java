package com.transaction.repository;

import com.transaction.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query("SELECT s FROM Sale s WHERE " +
            "(:startDate IS NULL OR s.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR s.createdAt <= :endDate) AND " +
            "(:productId IS NULL OR s.product.id = :productId) AND " +
            "(:flavourId IS NULL OR s.flavour.id = :flavourId) AND " +
            "(:parcel IS NULL OR s.parcel = :parcel)")
    List<Sale> findByFilters(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("productId") Long productId,
            @Param("flavourId") Long flavourId,
            @Param("parcel") Boolean parcel
    );

}
