package com.transaction.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "units")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Unit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // e.g., "Litre", "Packet"

    @Column(nullable = false)
    private String abbreviation; // e.g., "L", "Pkt"

    @Column(nullable = false)
    private Double conversionFactor;
    // conversion to a base unit if needed (e.g., 1 L = 1000 ml → factor = 1000)

    private String baseUnit;
    // e.g., "ml" for "Litre"

    private String unitType; // e.g., "Volume", "Weight", "Count"
}
