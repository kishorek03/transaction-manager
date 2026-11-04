package com.transaction.repository;

import com.transaction.dto.projection.PeriodAggregateProjection;
import com.transaction.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query("SELECT s FROM Sale s WHERE " +
            "(:startDate IS NULL OR s.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR s.createdAt < :endDate) AND " +
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

    @Query(value = "SELECT s FROM Sale s WHERE " +
            "(:startDate IS NULL OR s.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR s.createdAt < :endDate) AND " +
            "(:productId IS NULL OR s.product.id = :productId) AND " +
            "(:flavourId IS NULL OR s.flavour.id = :flavourId) AND " +
            "(:parcel IS NULL OR s.parcel = :parcel) " +
            "ORDER BY s.createdAt DESC",
            countQuery = "SELECT COUNT(s) FROM Sale s WHERE " +
            "(:startDate IS NULL OR s.createdAt >= :startDate) AND " +
            "(:endDate IS NULL OR s.createdAt < :endDate) AND " +
            "(:productId IS NULL OR s.product.id = :productId) AND " +
            "(:flavourId IS NULL OR s.flavour.id = :flavourId) AND " +
            "(:parcel IS NULL OR s.parcel = :parcel)")
    Page<Sale> findByFilters(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("productId") Long productId,
            @Param("flavourId") Long flavourId,
            @Param("parcel") Boolean parcel,
            Pageable pageable
    );

    @Query(value = """
        SELECT 
            DATE_FORMAT(s.created_at, :dateFormat) AS period,
            COALESCE(SUM(s.quantity), 0) AS total,
            COUNT(s.id) AS count
        FROM sale s
        JOIN product p ON s.product_id = p.id
        WHERE s.created_at >= :start AND s.created_at < :end
        AND (s.product_id = 5 OR LOWER(p.name) LIKE '%sugarcane%' OR LOWER(p.name) LIKE '%cane%' OR LOWER(p.name) LIKE '%karumbu%')
        GROUP BY DATE_FORMAT(s.created_at, :dateFormat)
        ORDER BY period ASC
    """, nativeQuery = true)
    List<PeriodAggregateProjection> getSugarcaneJuiceSalesByPeriod(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("dateFormat") String dateFormat
    );

    @Query(value = """
        SELECT 
            DATE_FORMAT(s.created_at, :dateFormat) AS period,
            COALESCE(SUM(s.quantity), 0) AS total,
            COUNT(s.id) AS count
        FROM sale s
        JOIN product p ON s.product_id = p.id
        WHERE s.created_at >= :start AND s.created_at < :end
        AND (LOWER(p.name) LIKE '%butter%' OR LOWER(p.name) LIKE '%mor%' OR LOWER(p.name) LIKE '%curd%')
        GROUP BY DATE_FORMAT(s.created_at, :dateFormat)
        ORDER BY period ASC
    """, nativeQuery = true)
    List<PeriodAggregateProjection> getButtermilkSalesByPeriod(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("dateFormat") String dateFormat
    );

    @Query("""
    SELECT SUM(CASE WHEN s.salePrice IS NOT NULL AND s.salePrice > 0 THEN s.salePrice ELSE s.amount END)
                                                           FROM Sale s
                                                           WHERE DATE(s.createdAt) = :date
                                                           AND s.order.paymentMethod.name = 'CASH'
""")
    Optional<BigDecimal> getTotalSalesByDateAndCash(@Param("date") LocalDate date);
}
