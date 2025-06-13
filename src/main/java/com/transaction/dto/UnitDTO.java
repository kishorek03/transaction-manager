package com.transaction.dto;

public record UnitDTO(
        long id,
    String name,
    String abbreviation,
    Double conversionFactor,
    String baseUnit,
    String unitType
) {}