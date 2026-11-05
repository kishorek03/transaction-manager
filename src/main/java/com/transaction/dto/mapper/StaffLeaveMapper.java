package com.transaction.dto.mapper;

import com.transaction.dto.StaffLeaveDTO;
import com.transaction.entity.StaffLeave;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StaffLeaveMapper {
    StaffLeaveDTO toDto(StaffLeave entity);
    StaffLeave toEntity(StaffLeaveDTO dto);
}
