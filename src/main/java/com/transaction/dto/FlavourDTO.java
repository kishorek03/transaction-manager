package com.transaction.dto;

import java.math.BigDecimal;

// Flavour DTO
public record FlavourDTO(Long id,String name, BigDecimal price) {}