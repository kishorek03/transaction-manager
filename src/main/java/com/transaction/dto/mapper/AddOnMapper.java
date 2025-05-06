package com.transaction.dto.mapper;

import com.transaction.dto.AddOnDTO;
import com.transaction.entity.AddOn;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddOnMapper {

    AddOnMapper INSTANCE = Mappers.getMapper(AddOnMapper.class);

    AddOnDTO toDTO(AddOn addOn);

    AddOn toEntity(AddOnDTO addOnDTO);
}
