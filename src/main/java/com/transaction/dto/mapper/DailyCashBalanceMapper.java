package com.transaction.dto.mapper;

import com.transaction.dto.DailyCashBalanceDTO;
import com.transaction.entity.DailyCashBalance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DailyCashBalanceMapper {
    DailyCashBalanceDTO toDto(DailyCashBalance entity);
    DailyCashBalance toEntity(DailyCashBalanceDTO dto);
}
