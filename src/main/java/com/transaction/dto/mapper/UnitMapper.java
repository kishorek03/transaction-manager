package com.transaction.dto.mapper;

import com.transaction.dto.UnitDTO;
import com.transaction.entity.Unit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UnitMapper {

    Unit toEntity(UnitDTO dto);

    UnitDTO toDto(Unit unit);
}