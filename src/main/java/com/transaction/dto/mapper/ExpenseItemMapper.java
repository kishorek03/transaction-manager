package com.transaction.dto.mapper;

import com.transaction.dto.ExpenseItemDTO;
import com.transaction.entity.Category;
import com.transaction.entity.ExpenseItem;
import com.transaction.entity.Unit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseItemMapper {

    @Mapping(target = "category", source = "categoryId")
    @Mapping(target = "defaultUnit", source = "defaultUnitId")
    ExpenseItem toEntity(ExpenseItemDTO dto);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.categoryName")
    @Mapping(target = "defaultUnitId", source = "defaultUnit.id")
    @Mapping(target = "defaultUnitName", source = "defaultUnit.name")
    ExpenseItemDTO toDto(ExpenseItem entity);

    default Unit mapUnit(Long id) {
        if (id == null) return null;
        Unit unit = new Unit();
        unit.setId(id);
        return unit;
    }

    default Category mapCategory(Long id) {
        if (id == null) return null;
        Category category = new Category();
        category.setId(id);
        return category;
    }
}
