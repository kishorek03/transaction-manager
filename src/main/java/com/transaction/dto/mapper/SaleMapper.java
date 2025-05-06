package com.transaction.dto.mapper;

import com.transaction.dto.SaleDTO;
import com.transaction.entity.*;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(source = "productId", target = "product", qualifiedByName = "mapProduct")
    @Mapping(source = "flavourId", target = "flavour", qualifiedByName = "mapFlavour")
    @Mapping(source = "addOnId", target = "addOn", qualifiedByName = "mapAddOn")
    @Mapping(source = "orderId", target = "order", qualifiedByName = "mapOrder")
    Sale toEntity(SaleDTO dto);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "flavour.id", target = "flavourId")
    @Mapping(source = "addOn.id", target = "addOnId")
    @Mapping(source = "order.id", target = "orderId")
    SaleDTO toDto(Sale entity);

    @Named("mapProduct")
    default Product mapProduct(Long id) {
        if (id == null) return null;
        Product product = new Product();
        product.setId(id);
        return product;
    }

    @Named("mapFlavour")
    default Flavour mapFlavour(Long id) {
        if (id == null) return null;
        Flavour flavour = new Flavour();
        flavour.setId(id);
        return flavour;
    }

    @Named("mapAddOn")
    default AddOn mapAddOn(Long id) {
        if (id == null) return null;
        AddOn addOn = new AddOn();
        addOn.setId(id);
        return addOn;
    }

    @Named("mapOrder")
    default Order mapOrder(Long id) {
        if (id == null) return null;
        Order order = new Order();
        order.setId(id);
        return order;
    }
}

