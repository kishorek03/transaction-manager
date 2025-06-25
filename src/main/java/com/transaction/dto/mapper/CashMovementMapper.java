package com.transaction.dto.mapper;

import com.transaction.dto.CashMovementDTO;
import com.transaction.entity.CashMovement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CashMovementMapper {
    CashMovementDTO toDto(CashMovement entity);
    CashMovement toEntity(CashMovementDTO dto);
}
