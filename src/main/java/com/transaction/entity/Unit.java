package com.transaction.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "unit")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Unit extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // e.g., "Litre", "Packet"

    @Column
    private String abbreviation; // e.g., "L", "Pkt"

    @Column
    private Double conversionFactor;
    // conversion to a base unit if needed (e.g., 1 L = 1000 ml → factor = 1000)

    private String baseUnit;
    // e.g., "ml" for "Litre"

    @Enumerated(EnumType.STRING)
    private UnitType unitType; // e.g., "Volume", "Weight", "Count"

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public Double getConversionFactor() {
        return conversionFactor;
    }

    public void setConversionFactor(Double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public String getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(String baseUnit) {
        this.baseUnit = baseUnit;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }
}
