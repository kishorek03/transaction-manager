package com.transaction.dto.mapper;

import com.transaction.dto.ShopHolidayDTO;
import com.transaction.entity.ShopHoliday;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShopHolidayMapper {
    ShopHolidayDTO toDto(ShopHoliday entity);
    ShopHoliday toEntity(ShopHolidayDTO dto);
}
