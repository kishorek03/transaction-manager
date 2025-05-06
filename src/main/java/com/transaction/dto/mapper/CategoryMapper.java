package com.transaction.dto.mapper;


import com.transaction.dto.CategoryDTO;
import com.transaction.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO toDto(Category category);

    Category toEntity(CategoryDTO dto);
}
