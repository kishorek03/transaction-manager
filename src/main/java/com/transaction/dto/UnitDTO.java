package com.transaction.dto;

public record UnitDTO(
    String name,
    String abbreviation,
    Double conversionFactor,
    String baseUnit,
    String unitType
) {}