package com.transaction.dto.mapper;

import com.transaction.dto.ExpenseDTO;
import com.transaction.entity.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    // DTO -> Entity
    @Mapping(target = "category.id", source = "categoryId")
    @Mapping(target = "paymentMethod.id", source = "paymentMethodId")
    @Mapping(target = "unit.id", source = "unitId")
    @Mapping(target = "unit.name", ignore = true) // will be set from DB
    @Mapping(target = "category.categoryName", ignore = true) // will be set in service
    @Mapping(target = "paymentMethod.name", ignore = true)
    Expense toEntity(ExpenseDTO dto);
    // Entity -> DTO
    @Mapping(target = "unitId", source = "unit.id")
    @Mapping(target = "unitName", source = "unit.name")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.categoryName")
    @Mapping(target = "paymentMethodId", source = "paymentMethod.id")
    @Mapping(target = "paymentMethodName", source = "paymentMethod.name")
    @Mapping(source = "createdAt", target = "createdAt")
    ExpenseDTO toDto(Expense expense);
}