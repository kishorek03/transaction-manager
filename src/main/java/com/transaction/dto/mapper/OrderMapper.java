package com.transaction.dto.mapper;

import com.transaction.dto.OrderDTO;
import com.transaction.entity.Order;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {SaleMapper.class})
public interface OrderMapper {

    @Mapping(target = "paymentMethodId", expression = "java(order.getPaymentMethod() != null ? order.getPaymentMethod().getId() : null)")
    OrderDTO toDTO(Order order);

    @InheritInverseConfiguration
    Order toEntity(OrderDTO dto);
}
