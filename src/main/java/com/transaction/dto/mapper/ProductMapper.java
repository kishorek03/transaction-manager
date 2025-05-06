package com.transaction.dto.mapper;

import com.transaction.dto.ProductDTO;
import com.transaction.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "unitId", target = "unit")
    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "flavourIds", target = "flavours")
    @Mapping(source = "addOnIds", target = "addOns")
    Product toEntity(ProductDTO dto);

    @Mapping(source = "unit.id", target = "unitId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "flavours", target = "flavourIds")
    @Mapping(source = "addOns", target = "addOnIds")
    ProductDTO toDto(Product entity);

    // Map Long → Unit
    default Unit mapUnit(Long id) {
        if (id == null) return null;
        Unit unit = new Unit();
        unit.setId(id);
        return unit;
    }

    // Map Long → Category
    default Category mapCategory(Long id) {
        if (id == null) return null;
        Category category = new Category();
        category.setId(id);
        return category;
    }

    // Map List<Long> → List<Flavour>
    default List<Flavour> mapFlavours(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream().map(id -> {
            Flavour flavour = new Flavour();
            flavour.setId(id);
            return flavour;
        }).collect(Collectors.toList());
    }

    // Map List<Long> → List<AddOn>
    default List<AddOn> mapAddOns(List<Long> ids) {
        if (ids == null) return null;
        return ids.stream().map(id -> {
            AddOn addOn = new AddOn();
            addOn.setId(id);
            return addOn;
        }).collect(Collectors.toList());
    }

    // Reverse mapping: Flavour → Long
    default List<Long> mapFlavourIds(List<Flavour> flavours) {
        if (flavours == null) return null;
        return flavours.stream()
                .map(Flavour::getId)
                .collect(Collectors.toList());
    }

    // Reverse mapping: AddOn → Long
    default List<Long> mapAddOnIds(List<AddOn> addOns) {
        if (addOns == null) return null;
        return addOns.stream()
                .map(AddOn::getId)
                .collect(Collectors.toList());
    }
}
