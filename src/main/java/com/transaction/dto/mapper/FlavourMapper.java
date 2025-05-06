package com.transaction.dto.mapper;

import com.transaction.dto.FlavourDTO;
import com.transaction.entity.Flavour;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FlavourMapper {

    Flavour toEntity(FlavourDTO dto);

    FlavourDTO toDto(Flavour entity);
}
